package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.dto.Request.CourseRequest;
import com.binar.pedulibelajar.dto.Response.CourseResponse;
import com.binar.pedulibelajar.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<CourseResponse> getAllCourses() {
       List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponse getCourseByCourseCode(String courseCode) {
        Optional<Course> course = courseRepository.findByCourseCode(courseCode);
        return course.map(this::mapToResponse).get();
    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest) {
        Course course = mapToEntity(courseRequest);
        Course savedCourse = courseRepository.save(course);
        return mapToResponse(savedCourse);
    }

    @Override
    public CourseResponse updateCourse(String courseCode, CourseRequest courseRequest) {
        return courseRepository.findByCourseCode(courseCode)
                .map(existingCourse -> {
                    existingCourse.setName(courseRequest.getName());
                    existingCourse.setCourseCode(courseRequest.getCourseCode());
                    existingCourse.setLevel(courseRequest.getLevel());
                    existingCourse.setType(courseRequest.getType());
                    existingCourse.setCategory(courseRequest.getCategory());
                    existingCourse.setDescription(courseRequest.getDescription());
                    existingCourse.setPrice(courseRequest.getPrice());
                    existingCourse.setAuthor(courseRequest.getAuthor());
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return mapToResponse(updatedCourse);
                }).orElse(null);
    }

    @Override
    public void deleteCourse(String courseCode) {
        courseRepository.findByCourseCode(courseCode).ifPresent(course ->
            courseRepository.delete(course));
    }

    private CourseResponse mapToResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setName(course.getName());
        response.setCourseCode(course.getCourseCode());
        response.setCategory(course.getCategory());
        response.setType(course.getType());
        response.setLevel(course.getLevel());
        response.setPrice(course.getPrice());
        response.setDescription(course.getDescription());
        response.setAuthor(course.getAuthor());
        return response;
    }

    private Course mapToEntity(CourseRequest courseRequest) {
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setCourseCode(courseRequest.getCourseCode());
        course.setCategory(courseRequest.getCategory());
        course.setType(courseRequest.getType());
        course.setLevel(courseRequest.getLevel());
        course.setPrice(courseRequest.getPrice());
        course.setDescription(courseRequest.getDescription());
        course.setAuthor(courseRequest.getAuthor());
        return course;
    }
}
