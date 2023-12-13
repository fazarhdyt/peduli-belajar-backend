package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import lombok.Data;

@Data
public class PaymentHistoryResponse {

    private CourseCategory category;
    private String title;
    private String teacher;
    private CourseLevel level;
    private String status;

}
