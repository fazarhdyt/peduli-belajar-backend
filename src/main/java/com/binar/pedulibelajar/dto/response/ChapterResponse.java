package com.binar.pedulibelajar.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChapterResponse {

    private Long id;
    private Long courseId;
    private int chapterNo;
    private String chapterTitle;
    private List<SubjectResponse> subject;
}

