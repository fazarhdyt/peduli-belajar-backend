package com.binar.pedulibelajar.dto.request;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import lombok.Data;

@Data
public class CategoryRequest {

    private CourseCategory categoryName;
}
