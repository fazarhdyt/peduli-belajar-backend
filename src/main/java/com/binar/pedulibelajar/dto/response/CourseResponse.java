package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

@Data
public class CourseResponse {

    private String id;
    private String title;
    private String courseCode;
    private CategoryResponse category;
    private Type type;
    private CourseLevel level;
    private Long price;
}