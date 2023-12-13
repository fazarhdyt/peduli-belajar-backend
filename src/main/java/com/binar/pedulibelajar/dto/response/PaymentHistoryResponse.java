package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import lombok.Data;

@Data
public class PaymentHistoryResponse {

    private String courseCode;
    private String thumbnail;
    private CourseCategory category;
    private String title;
    private String teacher;
    private int modul;
    private CourseLevel level;
    private double rating;
    private String status;

}
