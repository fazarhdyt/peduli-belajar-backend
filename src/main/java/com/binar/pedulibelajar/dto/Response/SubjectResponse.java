package com.binar.pedulibelajar.dto.Response;

import lombok.Data;

@Data
public class SubjectResponse {

    private Long id;
    private Long chapterId;
    private int subjectNo;
    private String videoTitle;
    private String videoLink;
    private String materialTypeName;
}
