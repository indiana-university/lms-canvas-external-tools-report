package edu.iu.uits.lms.externaltoolsreport.services;

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
