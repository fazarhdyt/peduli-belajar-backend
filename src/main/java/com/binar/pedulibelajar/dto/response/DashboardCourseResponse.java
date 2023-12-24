package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import lombok.Data;

@Data
public class DashboardCourseResponse {

    private String id;
    private String courseCode;
    private String title;
    private CategoryResponse category;
    private String teacher;
    private CourseLevel level;
    private int modul;
    private double rating;
    private double price;
}
