package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

import java.util.List;

@Data
public class DetailCourseResponse {

    private String id;
    private String title;
    private String courseCode;
    private CategoryResponse category;
    private Type type;
    private CourseLevel level;
    private Double price;
    private String description;
    private String teacher;
    private int modul;
    private double rating;
    private String telegramLink;
    private List<ChapterResponse> chapter;
}
