package com.binar.pedulibelajar.dto.request;

import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

@Data
public class SubjectRequest {

    private int subjectNo;
    private String videoTitle;
    private String videoLink;
    private Type subjectType;
}
