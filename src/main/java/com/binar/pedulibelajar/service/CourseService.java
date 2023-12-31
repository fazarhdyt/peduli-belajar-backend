package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.request.EditCourseRequest;
import com.binar.pedulibelajar.dto.response.*;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<DashboardCourseResponse> getAllCourses();

    DetailCourseResponse getCourseByCourseCode(String courseCode);

    PaginationCourseResponse<DashboardCourseResponse> getCourseByFilters(Integer page, Integer size,
            List<CourseCategory> categories, List<CourseLevel> levels, List<Type> types, String title, Boolean sortByDate, Boolean sortByPurchase);

    PaginationCourseResponse<DashboardMyCourseResponse> getMyCourse(Integer page, Integer size,
            List<CourseCategory> categories, List<CourseLevel> levels, List<Type> types, Boolean completed, String title, Boolean sortByDate, Boolean sortByPurchaseCount);

    CourseResponse createCourse(CourseRequest courseRequest);

    CourseResponse updateCourse(String courseCode, EditCourseRequest editCourseRequest);

    void deleteCourse(String courseCode);

    long getTotalCourse();

    long getPremiumCourse();

    List<CourseResponse> getManageCourses(Type type, String title);

    Map<String, Double> getProgress(String courseCode);
}