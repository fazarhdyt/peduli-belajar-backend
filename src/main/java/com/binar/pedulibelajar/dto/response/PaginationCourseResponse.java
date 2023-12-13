package com.binar.pedulibelajar.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PaginationCourseResponse {

    List<DashboardCourseResponse> courses;
    Integer currentPage;
    Integer totalPage;
    Long totalCourse;

}
