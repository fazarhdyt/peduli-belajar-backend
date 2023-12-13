package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.PaymentMethod;
import lombok.Data;

import java.util.Date;

@Data
public class StatusOrderResponse {

    private String username;
    private CourseCategory category;
    private String title;
    private String status;
    private PaymentMethod paymentMethod;
    private String paymentDate;
}
