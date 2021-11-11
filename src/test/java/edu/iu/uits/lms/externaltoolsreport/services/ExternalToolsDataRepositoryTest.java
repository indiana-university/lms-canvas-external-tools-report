package edu.iu.uits.lms.externaltoolsreport.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import edu.iu.uits.lms.externaltoolsreport.config.ToolConfig;
import edu.iu.uits.lms.externaltoolsreport.model.ExternalToolsData;
import edu.iu.uits.lms.externaltoolsreport.repository.ExternalToolsDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class ExternalToolsDataRepositoryTest {

   @MockBean
   private ToolConfig toolConfig;

   @Autowired
   private ExternalToolsDataRepository externalToolsDataRepository;
   
   private static final String FALL="5432";
   private static final String SPRING="1234";
   private static final String SUMMER="4321";
   

   @Test
   public void testRead() throws Exception {
      ExternalToolsData etd = externalToolsDataRepository.findById(1L).orElse(null);
      Assert.assertNotNull(etd);
      Assert.assertEquals("canvas id doesn't match", 1963585, etd.getCanvasId().longValue());
      
      List<ExternalToolsData> allData = (List<ExternalToolsData>)externalToolsDataRepository.findAll();
      Assert.assertEquals(6, allData.size());
   }
   
   @Test
   public void testGetDistinctTermsByCreatedDesc() throws Exception {
      List<String> expectedTerms = Arrays.asList(FALL, SUMMER, SPRING);
      List<Object[]> results = externalToolsDataRepository.getDistinctTermByCreatedDesc();
      Assert.assertEquals(3, results.size());
      for (int i=0; i < expectedTerms.size(); i++) {
         Assert.assertEquals(expectedTerms.get(i), results.get(i)[0]);
      }
   }
   
   @Test
   public void testGetByTerm() throws Exception {
      Assert.assertEquals(0, externalToolsDataRepository.findByTerm("").size());
      Assert.assertEquals(3, externalToolsDataRepository.findByTerm("Spring 2021").size());
      Assert.assertEquals(2, externalToolsDataRepository.findByTerm("Summer 2021").size());
      Assert.assertEquals(1, externalToolsDataRepository.findByTerm("Fall 2021").size());
   }
}
