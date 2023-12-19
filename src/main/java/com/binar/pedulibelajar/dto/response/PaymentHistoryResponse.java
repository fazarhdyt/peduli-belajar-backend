package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseLevel;
import lombok.Data;

@Data
public class PaymentHistoryResponse {

    private String courseCode;
    private CategoryResponse category;
    private String title;
    private String teacher;
    private int modul;
    private CourseLevel level;
    private double rating;
    private String status;

}
