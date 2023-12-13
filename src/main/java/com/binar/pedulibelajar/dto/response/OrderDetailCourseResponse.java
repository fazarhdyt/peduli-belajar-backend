package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailCourseResponse {

    private CourseCategory category;
    private String courseTitle;
    private String authorCourse;
    private double price;
    private double tax;
    private double totalPrice;
}
