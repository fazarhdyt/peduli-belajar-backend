package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CourseResponse {

    private String title;
    private String courseCode;
    private CourseCategory category;
    private Type type;
    private CourseLevel level;
    private double price;
    private String description;
    private String teacher;
    private MultipartFile thumbnail;
    private List<ChapterResponse> chapter;
}
