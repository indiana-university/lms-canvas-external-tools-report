package edu.iu.uits.lms.externaltoolsreport.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import edu.iu.uits.lms.externaltoolsreport.config.ToolConfig;
import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.service.ExternalToolsReportService;
import edu.iu.uits.lms.externaltoolsreport.util.ExternalToolsReportUtils;
import edu.iu.uits.lms.lti.LTIConstants;
import edu.iu.uits.lms.lti.controller.LtiAuthenticationTokenAwareController;
import edu.iu.uits.lms.lti.security.LtiAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/app")
@Slf4j
public class ToolController extends LtiAuthenticationTokenAwareController {

   @Autowired
   private ToolConfig toolConfig = null;

   @Autowired
   private ResourceBundleMessageSource messageSource = null;
   
   @Autowired
   private ExternalToolsReportService externalToolsReportService;

   public enum RequiredHeadings {
      canvas_id,
      sis_course_id,
      campus,
      course_code,
      term,
      tool,
      placement;
   }

   @RequestMapping("/index")
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView index(Model model, HttpServletRequest request) {
      log.debug("in /index");
      LtiAuthenticationToken token = getTokenWithoutContext();
      
      List<String> terms = externalToolsReportService.getDistinctTerms();
      model.addAttribute("terms", terms);

      return new ModelAndView("index");
   }
   

   @RequestMapping(value = "/externaltools", params="action=delete", method = RequestMethod.POST)
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView delete(Model model, 
                              HttpServletRequest request,
                              @RequestParam("termToDelete") String termToDelete) {
      
      if (termToDelete == null || termToDelete.isBlank()) {
         model.addAttribute("error", messageSource.getMessage("externaltools.delete.error", new Object[] {}, Locale.getDefault()));
      } else {
         externalToolsReportService.deleteAllRecordsForTerm(termToDelete);
         model.addAttribute("successMsg", messageSource.getMessage("externaltools.delete.success", new Object[]{termToDelete}, Locale.getDefault()));
      }
      
      return index(model, request);
   }

   @RequestMapping(value = "/externaltools", params="action=upload", method = RequestMethod.POST)
   @Secured(LTIConstants.INSTRUCTOR_AUTHORITY)
   public ModelAndView uploadNewTerm(Model model, 
                                     HttpServletRequest request,
                                     @RequestParam(value = "newTermFile") MultipartFile newTermFile) {
      
      if (newTermFile.isEmpty()) {
         model.addAttribute("error", messageSource.getMessage("externaltools.upload.error.noFile", new Object[] {}, Locale.getDefault()));
         return index(model, request);
      } 
      
      try {
         CSVReader csvReader = new CSVReader(new InputStreamReader(newTermFile.getInputStream()));
         List<String[]> rawContents = csvReader.readAll();
         int numRows = rawContents.size();

         // make sure the file isn't empty and has at least 2 rows (one for header, one for term data)
         if (rawContents == null || rawContents.isEmpty() || rawContents.size() < 2) {
            model.addAttribute("error", messageSource.getMessage("externaltools.upload.error.noContent", new Object[] {}, Locale.getDefault()));
            return index(model, request);
         } 

         List<String> headers = new ArrayList(Arrays.asList(rawContents.get(0)));
         
         // check for required header columns
         String errorMsg = "";
         for (RequiredHeadings heading : RequiredHeadings.values()) {
            if (!headers.stream().anyMatch(s -> s.equalsIgnoreCase(heading.toString()))) {
               errorMsg += "CSV is missing required column: " + heading + System.lineSeparator();
            }
         }
         
         if (errorMsg.length() > 0) {
            model.addAttribute("error", errorMsg);
            return index(model, request);
         }
         
         List<String> existingTerms = externalToolsReportService.getDistinctTerms();
         
         List<ExternalToolsData> newData = new ArrayList<>();
         int currRow = 1;
         while (currRow < numRows) {
            ExternalToolsData data = new ExternalToolsData();
            Map<String, String> csvContent = ExternalToolsReportUtils.createCsvLineDataMap(rawContents, currRow);

            // Ensure the canvas id is numeric
            String canvasIdString = csvContent.get(RequiredHeadings.canvas_id.toString());
            if (!NumberUtils.isCreatable(canvasIdString)) {
               model.addAttribute("error", messageSource.getMessage("externaltools.upload.error.nonNumeric", new Object[] {currRow, canvasIdString}, Locale.getDefault()));
               return index(model, request);
            }
            data.setCanvasId(Long.parseLong(canvasIdString));
            
            data.setSisCourseId(csvContent.get(RequiredHeadings.sis_course_id.toString()));
            data.setCampus(csvContent.get(RequiredHeadings.campus.toString()));
            data.setCourseCode(csvContent.get(RequiredHeadings.course_code.toString()));

            // Ensure this term isn't already in the database
            String term = csvContent.get(RequiredHeadings.term.toString());
            if (existingTerms.stream().anyMatch(s -> s.equalsIgnoreCase(term))) {
               model.addAttribute("error", messageSource.getMessage("externaltools.upload.error.existingTerm", new Object[] {term}, Locale.getDefault()));
               return index(model, request);
            }
            data.setTerm(term);
            
            data.setTool(csvContent.get(RequiredHeadings.tool.toString()));
            data.setPlacement(csvContent.get(RequiredHeadings.placement.toString()));
            
            newData.add(data);
            
            currRow++;
         }
         
         externalToolsReportService.saveToolData(newData);

         model.addAttribute("successMsg",  messageSource.getMessage("externaltools.upload.success", new Object[] {}, Locale.getDefault()));
      
      } catch (IOException | CsvException e) {
            log.error("Exception uploading csv file for External Tools Data tool", e);
            model.addAttribute("error", messageSource.getMessage("exernaltools.upload.error.unexpected", new Object[] {}, Locale.getDefault()));
      } 
      
      return index(model, request);
   }
}
