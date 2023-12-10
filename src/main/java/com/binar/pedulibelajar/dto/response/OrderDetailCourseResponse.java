package com.binar.pedulibelajar.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailCourseResponse {

    private String category;
    private String courseTitle;
    private String authorCourse;
    private double price;
    private double tax;
    private double totalPrice;
}
