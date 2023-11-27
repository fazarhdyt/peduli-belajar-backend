package com.binar.pedulibelajar.dto.Response;

import lombok.Data;

@Data
public class CourseResponse {

    private String id;
    private String name;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String author;
}

