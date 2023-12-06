package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.CourseResponse;
import com.binar.pedulibelajar.dto.response.CreateCourseResponse;
import com.binar.pedulibelajar.dto.response.OrderDetailCourseResponse;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseByCourseCode(String courseCode);
    CreateCourseResponse createCourse(CourseRequest courseRequest);
    CreateCourseResponse updateCourse(String courseCode,  CourseRequest courseRequest);
    void deleteCourse(String courseCode);
    OrderDetailCourseResponse getOrderDetailCourse(String courseCode);
}
