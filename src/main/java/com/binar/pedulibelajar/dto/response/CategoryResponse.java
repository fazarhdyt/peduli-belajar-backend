package com.binar.pedulibelajar.dto.response;

import com.binar.pedulibelajar.enumeration.CourseCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private CourseCategory categoryName;
    private String categoryImage;
}
