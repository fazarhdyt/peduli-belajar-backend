package com.binar.pedulibelajar.dto.request;

import lombok.Data;

@Data
public class SubjectRequest {

    private int subjectNo;
    private String videoTitle;
    private String videoLink;
    private SubjectTypeRequest subjectType;
}
