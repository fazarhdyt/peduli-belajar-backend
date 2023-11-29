package com.binar.pedulibelajar.dto.Request;

import lombok.Data;

@Data
public class CourseRequest {

    private String name;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String author;
}
