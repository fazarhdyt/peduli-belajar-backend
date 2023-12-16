package com.binar.pedulibelajar.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailCourseResponse {

    private CategoryResponse category;
    private String courseTitle;
    private String teacher;
    private double price;
    private double tax;
    private double totalPrice;
}
