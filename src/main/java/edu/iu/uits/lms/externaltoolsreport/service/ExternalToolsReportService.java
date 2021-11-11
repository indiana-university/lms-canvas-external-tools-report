package edu.iu.uits.lms.externaltoolsreport.service;

import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.model.TermData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalToolsReportService {
    
    private static final int BATCH_SIZE = 500;
    
    @Autowired
    private ExternalToolsDataRepository externalToolsDataRepository;

    /**
     * @param term 
     * @return All of the external tool data records for the given term
     */
    public List<ExternalToolsData> getToolDataForTerm(String term) {
        if (term == null) {
            throw new IllegalArgumentException("Null term passed to ExternalToolsReportService.getToolDataForTerm");
        }
        
        return externalToolsDataRepository.findByTerm(term);
    }

    /**
     * @return All of the distinct terms in the data table ordered by created date descending
     */
    public List<TermData> getDistinctTerms() {
        List<TermData> orderedTerms = new ArrayList<>();
        List<Object[]> results =  externalToolsDataRepository.getDistinctTermByCreatedDesc();
        if (results != null) {
            for (Object[] result : results) {
                String termId = (String)result[0];
                String termName = getTermName(termId);
                TermData term = new TermData(termId, termName);
                orderedTerms.add(term);
            }
        }
        
        return orderedTerms;
    }
    
    public void saveToolData(List<ExternalToolsData> toolData) {
        List<List<ExternalToolsData>> partitions = ListUtils.partition(toolData, BATCH_SIZE);
        for (List<ExternalToolsData> batch : partitions) {
            externalToolsDataRepository.saveAll(batch);
        }

        //externalToolsDataRepository.saveAll(toolData);
    }

    /**
     * Deletes all records for the given term
     * @param termId
     */
    public void deleteAllRecordsForTerm(String termId) {
        if (termId == null) {
            throw new IllegalArgumentException("Null termId passed to ExternalToolsReportService.deleteTerm");
        }
        
        externalToolsDataRepository.deleteTerm(termId);
    }
    
    public String getTermName(String termId) {
        if (termId == null) {
            throw new IllegalArgumentException("Null termId passed to ExternalToolsReportService.getTermName");
        }
        ExternalToolsData toolData = externalToolsDataRepository.findFirstByTermId(termId);
        return toolData.getTerm();
    }
}
