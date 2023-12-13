package com.binar.pedulibelajar.dto.response;

import lombok.Data;

@Data
public class CreateCourseResponse {

    private String title;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private Long price;
}