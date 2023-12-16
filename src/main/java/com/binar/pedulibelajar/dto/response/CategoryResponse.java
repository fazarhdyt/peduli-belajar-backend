package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private CourseCategory categoryName;
    private String categoryImage;
}
