package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {

    private String title;
    private String courseCode;
    private CategoryResponse category;
    private Type type;
    private CourseLevel level;
    private double price;
    private String description;
    private String teacher;
    private List<ChapterResponse> chapter;
}
