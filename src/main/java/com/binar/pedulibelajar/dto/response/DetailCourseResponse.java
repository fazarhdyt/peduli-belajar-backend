package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

import java.util.List;

@Data
public class DetailCourseResponse {

    private String id;
    private String title;
    private String courseCode;
    private CourseCategory category;
    private Type type;
    private CourseLevel level;
    private double price;
    private String description;
    private String teacher;
    private int modul;
    private double rating;
    private List<ChapterResponse> chapter;
}
