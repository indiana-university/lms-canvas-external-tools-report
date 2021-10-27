package edu.iu.uits.lms.externaltoolsreport.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import edu.iu.uits.lms.externaltoolsreport.config.ToolConfig;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import edu.iu.uits.lms.externaltoolsreport.service.ExternalToolsReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class },
      mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@Slf4j
@ActiveProfiles("none")
@DatabaseSetup(value = "/externalToolsData.xml")
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL)
@ComponentScan("edu.iu.uits.lms.externaltoolsreport.service")
public class ExternalToolsReportServiceTest {

   @MockBean
   private ToolConfig toolConfig;

   @Autowired
   private ExternalToolsReportService externalToolsReportService;

   @Autowired
   private ExternalToolsDataRepository externalToolsDataRepository;
   
   @Test
   public void testGetDistinctTerms() throws Exception {
      List<String> expectedTerms = Arrays.asList("Fall 2021", "Summer 2021", "Spring 2021");
      List<String> actualTerms = externalToolsReportService.getDistinctTerms();
      Assert.assertEquals(3, actualTerms.size());
      for (int i=0; i < expectedTerms.size(); i++) {
         Assert.assertEquals(expectedTerms.get(i), actualTerms.get(i));
      }
   }

}
