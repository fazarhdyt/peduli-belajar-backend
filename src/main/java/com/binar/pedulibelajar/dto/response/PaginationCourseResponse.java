package com.binar.pedulibelajar.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PaginationCourseResponse<T> {

    List<T> courses;
    Integer currentPage;
    Integer totalPage;
    Long totalCourse;

}
