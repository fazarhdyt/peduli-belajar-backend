package com.binar.pedulibelajar.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CourseRequest {

    private String title;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String teacher;
    //private MultipartFile thumbnail;
    private List<ChapterRequest> chapter;
}
