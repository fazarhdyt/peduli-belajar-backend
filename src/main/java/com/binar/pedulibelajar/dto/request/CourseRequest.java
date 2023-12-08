package com.binar.pedulibelajar.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {

    private String title;
    private String courseCode;
    /*
    @Enumerated(EnumType.STRING)
    private ECategory category;
    @Enumerated(EnumType.STRING)
    private EType type;
    @Enumerated(EnumType.STRING)
    private ELevel level;
     */
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String teacher;
    private List<ChapterRequest> chapter;
}