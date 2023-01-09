package edu.iu.uits.lms.externaltoolsreport.services;

import edu.iu.uits.lms.externaltoolsreport.config.PostgresDBConfig;
import edu.iu.uits.lms.externaltoolsreport.config.ToolConfig;
import edu.iu.uits.lms.externaltoolsreport.model.TermData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import edu.iu.uits.lms.externaltoolsreport.service.ExternalToolsReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
@Slf4j
@Sql("/externalToolsData.sql")
@Import({ToolConfig.class, PostgresDBConfig.class})
@ActiveProfiles("externaltoolsreport")
public class ExternalToolsReportServiceTest {

   @Autowired
   private ExternalToolsReportService externalToolsReportService;

   @Autowired
   private ExternalToolsDataRepository externalToolsDataRepository;

   @MockBean
   private JwtDecoder jwtDecoder;
   
   @Test
   public void testGetDistinctTerms() throws Exception {
      List<String> expectedTerms = Arrays.asList("Fall 2021", "Summer 2021", "Spring 2021");
      List<TermData> actualTerms = externalToolsReportService.getDistinctTerms();
      Assertions.assertEquals(3, actualTerms.size());
      for (int i=0; i < expectedTerms.size(); i++) {
         Assertions.assertEquals(expectedTerms.get(i), actualTerms.get(i).getTermName());
      }
   }

   @TestConfiguration
   static class TestContextConfiguration {
      @Bean
      public ExternalToolsReportService reportsService() {
         return new ExternalToolsReportService();
      }
   }

}
