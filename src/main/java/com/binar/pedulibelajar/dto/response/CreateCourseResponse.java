package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

@Data
public class CreateCourseResponse {

    private String title;
    private String courseCode;
    private CourseCategory category;
    private Type type;
    private CourseLevel level;
    private Long price;
}