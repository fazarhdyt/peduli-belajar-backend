package com.binar.pedulibelajar.dto.Response;

import lombok.Data;

@Data
public class ChapterResponse {

    private Long id;
    private Long courseId;
    private int chapterNo;
    private String chapterTitle;
}

