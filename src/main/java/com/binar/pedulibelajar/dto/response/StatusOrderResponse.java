package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import lombok.Data;

@Data
public class StatusOrderResponse {

    private String username;
    private CourseCategory category;
    private String title;
    private String status;
    private String paymentMethod;
    private String paymentDate;
}
