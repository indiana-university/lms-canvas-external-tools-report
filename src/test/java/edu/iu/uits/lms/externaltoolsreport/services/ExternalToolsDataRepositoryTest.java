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
import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class ExternalToolsDataRepositoryTest {
   @Autowired
   private ExternalToolsDataRepository externalToolsDataRepository;

   @MockBean
   private JwtDecoder jwtDecoder;
   
   private static final String FALL="5432";
   private static final String SPRING="1234";
   private static final String SUMMER="4321";
   

   @Test
   public void testRead() throws Exception {
      ExternalToolsData etd = externalToolsDataRepository.findById(1L).orElse(null);
      Assertions.assertNotNull(etd);
      Assertions.assertEquals(1963585, etd.getCanvasId().longValue(), "canvas id doesn't match");
      
      List<ExternalToolsData> allData = (List<ExternalToolsData>)externalToolsDataRepository.findAll();
      Assertions.assertEquals(6, allData.size());
   }
   
   @Test
   public void testGetDistinctTermsByCreatedDesc() throws Exception {
      List<String> expectedTerms = Arrays.asList(FALL, SUMMER, SPRING);
      List<Object[]> results = externalToolsDataRepository.getDistinctTermByCreatedDesc();
      Assertions.assertEquals(3, results.size());
      for (int i=0; i < expectedTerms.size(); i++) {
         Assertions.assertEquals(expectedTerms.get(i), results.get(i)[0]);
      }
   }
   
   @Test
   public void testGetByTerm() throws Exception {
      Assertions.assertEquals(0, externalToolsDataRepository.findByTerm("").size());
      Assertions.assertEquals(3, externalToolsDataRepository.findByTerm("Spring 2021").size());
      Assertions.assertEquals(2, externalToolsDataRepository.findByTerm("Summer 2021").size());
      Assertions.assertEquals(1, externalToolsDataRepository.findByTerm("Fall 2021").size());
   }
}
