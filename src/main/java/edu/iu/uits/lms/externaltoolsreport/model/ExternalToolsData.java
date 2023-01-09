package edu.iu.uits.lms.externaltoolsreport.model;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.iu.uits.lms.common.date.DateFormatUtil;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@SequenceGenerator(name = "EXTERNAL_TOOLS_DATA_ID_SEQ", sequenceName = "EXTERNAL_TOOLS_DATA_ID_SEQ", allocationSize = 50)
@NamedQueries({
        @NamedQuery(name = "ExternalToolsData.findByTerm", query = "from ExternalToolsData where term = :term"),
        @NamedQuery(name = "ExternalToolsData.getDistinctTermByCreatedDesc", query = "SELECT etd.termId, etd.term, MAX(etd.created) FROM ExternalToolsData etd GROUP BY etd.termId, etd.term ORDER BY MAX(etd.created) DESC, etd.termId"),
        @NamedQuery(name = "ExternalToolsData.deleteTerm", query = "DELETE FROM ExternalToolsData WHERE termId = :termId")
})

@Data
@RequiredArgsConstructor
public class ExternalToolsData {

   @Id
   @GeneratedValue(generator = "EXTERNAL_TOOLS_DATA_ID_SEQ")
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

   @Column(name= "COUNTS")
   private int counts;

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
