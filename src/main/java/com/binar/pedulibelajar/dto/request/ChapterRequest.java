package com.binar.pedulibelajar.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChapterRequest {

    private int chapterNo;
    private String chapterTitle;
    private List<SubjectRequest> subject;
}
