package com.binar.pedulibelajar.Service;

import com.binar.pedulibelajar.dto.Request.CourseRequest;
import com.binar.pedulibelajar.dto.Response.CourseResponse;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseByCourseCode(String courseCode);
    CourseResponse createCourse(CourseRequest courseRequest);
    CourseResponse updateCourse(String courseCode,  CourseRequest courseRequest);
    void deleteCourse(String courseCode);
}
