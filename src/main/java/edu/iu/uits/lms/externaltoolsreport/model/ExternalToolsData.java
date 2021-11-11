package edu.iu.uits.lms.externaltoolsreport.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.iu.uits.lms.common.date.DateFormatUtil;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "EXTERNAL_TOOLS_DATA")
@SequenceGenerator(name = "EXTERNAL_TOOLS_DATA_ID_SEQ", sequenceName = "EXTERNAL_TOOLS_DATA_ID_SEQ", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "ExternalToolsData.findByTerm", query = "from ExternalToolsData where term = :term"),
        @NamedQuery(name = "ExternalToolsData.getDistinctTermByCreatedDesc", query = "SELECT etd.termId, MAX(etd.created) FROM ExternalToolsData etd GROUP BY etd.termId ORDER BY MAX(etd.created) DESC, etd.termId"),
        @NamedQuery(name = "ExternalToolsData.deleteTerm", query = "DELETE FROM ExternalToolsData WHERE termId = :termId")
})

@Data
@RequiredArgsConstructor
public class ExternalToolsData {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXTERNAL_TOOLS_DATA_ID_SEQ")
   @Column(name = "EXTERNAL_TOOLS_DATA_ID")
   private Long id;
   
   @Column(name = "CANVAS_ID")
   private Long canvasId;

   @Column(name = "SIS_COURSE_ID")
   private String sisCourseId;

   @Column(name = "COURSE_CODE")
   private String courseCode;

   @Column(name = "CAMPUS")
   private String campus;
   
   @Column(name = "TERM_ID")
   private String termId;

   @Column(name = "TERM")
   private String term;

   @Column(name = "TOOL")
   private String tool;

   @Column(name = "PLACEMENT")
   private String placement;

   @JsonFormat(pattern= DateFormatUtil.JSON_DATE_FORMAT)
   @Column(name = "CREATED")
   private Date created;

   @PreUpdate
   @PrePersist
   public void updateTimeStamps() {
      if (created == null) {
         created = new Date();
      }
   }
}
