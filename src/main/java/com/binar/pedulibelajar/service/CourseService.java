package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.DetailCourseResponse;
import com.binar.pedulibelajar.dto.response.CreateCourseResponse;

import com.binar.pedulibelajar.dto.response.OrderDetailCourseResponse;
import com.binar.pedulibelajar.dto.response.PaginationCourseResponse;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;

import java.util.List;

public interface CourseService {
    List<DetailCourseResponse> getAllCourses();

    DetailCourseResponse getCourseByCourseCode(String courseCode);

    PaginationCourseResponse getCourseByFilters(Integer page, Integer size, List<CourseCategory> category, List<CourseLevel> level, List<Type> type, String title);

    CreateCourseResponse createCourse(CourseRequest courseRequest);

    CreateCourseResponse updateCourse(String courseCode, CourseRequest courseRequest);

    void deleteCourse(String courseCode);

    long getTotalCourse();

    long getPremiumCourse();
}