package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import lombok.Data;

@Data
public class DashboardMyCourseResponse {
    private String courseCode;
    private String thumbnail;
    private String title;
    private CourseCategory category;
    private String teacher;
    private CourseLevel level;
    private int modul;
    private double rating;
    private double percentProgress;
}
