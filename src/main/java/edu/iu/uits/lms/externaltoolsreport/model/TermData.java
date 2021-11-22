package edu.iu.uits.lms.externaltoolsreport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TermData implements Serializable  {
    @NonNull
    private String termId;
    @NonNull
    private String termName;
}
