package com.binar.pedulibelajar.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChapterResponse {

    private String id;
    private int chapterNo;
    private String chapterTitle;
    private List<SubjectResponse> subject;
}

