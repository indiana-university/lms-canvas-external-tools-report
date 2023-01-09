package edu.iu.uits.lms.externaltoolsreport.services;

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
