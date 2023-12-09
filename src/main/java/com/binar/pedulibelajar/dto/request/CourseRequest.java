package com.binar.pedulibelajar.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {

    private String title;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String teacher;
    private List<ChapterRequest> chapter;
}
