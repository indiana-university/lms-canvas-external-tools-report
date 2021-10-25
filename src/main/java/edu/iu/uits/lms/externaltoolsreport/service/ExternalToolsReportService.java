package edu.iu.uits.lms.externaltoolsreport.service;

import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalToolsReportService {
    
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
    public List<String> getDistinctTerms() {
        List<String> orderedTerms = new ArrayList<>();
        List<Object[]> results =  externalToolsDataRepository.getDistinctTermByCreatedDesc();
        if (results != null) {
            for (Object[] result : results) {
                orderedTerms.add((String)result[0]);
            }
        }
        
        return orderedTerms;
    }
    
    public void saveToolData(List<ExternalToolsData> toolData) {
        externalToolsDataRepository.saveAll(toolData);
    }

    /**
     * Deletes all records for the given term
     * @param term
     */
    public void deleteAllRecordsForTerm(String term) {
        if (term == null) {
            throw new IllegalArgumentException("Null term passed to ExternalToolsReportService.deleteTerm");
        }
        
        externalToolsDataRepository.deleteTerm(term);
    }
}
