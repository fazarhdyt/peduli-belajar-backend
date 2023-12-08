package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.CourseResponse;
import com.binar.pedulibelajar.dto.response.CreateCourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseByCourseCode(String courseCode);

    Page<CourseResponse> getCourseByFilters(List<String> category, List<String> levels, List<String> types, Pageable pageable);
    //Page<CourseResponse> getCourseByFilters(List<ECategory> category, List<ELevel> level, List<EType> type, Pageable pageable);

    CreateCourseResponse createCourse(CourseRequest courseRequest);
    CreateCourseResponse updateCourse(String courseCode,  CourseRequest courseRequest);
    void deleteCourse(String courseCode);

}