package com.binar.pedulibelajar.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {

    private String title;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String teacher;
    private List<ChapterResponse> chapter;
}

