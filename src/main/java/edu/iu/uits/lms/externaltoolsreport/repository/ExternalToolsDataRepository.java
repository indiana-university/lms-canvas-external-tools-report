package edu.iu.uits.lms.externaltoolsreport.repository;

import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface ExternalToolsDataRepository extends PagingAndSortingRepository<ExternalToolsData, Long> {

    List<ExternalToolsData> findByTerm(@Param("term") String term);

    /**
     * 
     * @return List<Object[]> containing the distinct terms with their creation date ordered by date desc
     */
    List<Object[]> getDistinctTermByCreatedDesc();

    @Modifying
    @Transactional
    void deleteTerm(@Param("termId") String termId);
    
    ExternalToolsData findFirstByTermId(String termId);

}
