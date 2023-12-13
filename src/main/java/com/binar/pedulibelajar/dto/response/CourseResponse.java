package com.binar.pedulibelajar.dto.response;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CourseResponse {
    private String title;
    private String courseCode;
    private String category;
    private String type;
    private String level;
    private double price;
    private String description;
    private String teacher;
    private MultipartFile thumbnail;
    private List<ChapterResponse> chapter;
}
