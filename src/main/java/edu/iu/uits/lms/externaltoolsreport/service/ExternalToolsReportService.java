package edu.iu.uits.lms.externaltoolsreport.service;

/*-
 * #%L
 * external-tools-report
 * %%
 * Copyright (C) 2022 - 2023 Indiana University
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Indiana University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.model.TermData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalToolsReportService {
    
    private static final int BATCH_SIZE = 50;
    
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
                String termName = (String)result[1];
                TermData term = new TermData(termId, termName);
                orderedTerms.add(term);
            }
        }
        
        return orderedTerms;
    }
    
    @Transactional
    public void saveToolData(List<ExternalToolsData> toolData) {
        List<List<ExternalToolsData>> partitions = ListUtils.partition(toolData, BATCH_SIZE);
        for (List<ExternalToolsData> batch : partitions) {
            externalToolsDataRepository.saveAll(batch);
        }
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
