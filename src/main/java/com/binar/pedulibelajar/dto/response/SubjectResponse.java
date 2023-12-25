package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

@Data
public class SubjectResponse {

    private String id;
    private int subjectNo;
    private String videoTitle;
    private String videoLink;
    private Type subjectType;
    boolean done;
}