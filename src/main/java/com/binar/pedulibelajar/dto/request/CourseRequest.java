package com.binar.pedulibelajar.dto.request;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {

    private String title;
    private String courseCode;
    private CategoryRequest category;
    private Type type;
    private CourseLevel level;
    private double price;
    private String description;
    private String telegramLink;
    private List<ChapterRequest> chapter;
}
