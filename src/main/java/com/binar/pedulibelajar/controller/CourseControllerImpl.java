package com.binar.pedulibelajar.controller;
/*
import com.binar.pedulibelajar.Model.Course;
import com.binar.pedulibelajar.dto.Request.CourseRequest;
import com.binar.pedulibelajar.dto.Response.CourseResponse;
import com.binar.pedulibelajar.Repository.CourseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseControllerImpl implements CourseController {

    private final CourseRepository courseRepository;

    @Override
    @GetMapping("/get-all-course")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courses);
    }

    @Override
    @GetMapping("/get-course/{courseCode}")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@PathVariable String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .map(course -> ResponseEntity.ok(mapToResponse(course)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/create-course")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        Course course = mapToEntity(courseRequest);
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedCourse));
    }

    @Override
    @PutMapping("/update-courses/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable String id, @RequestBody CourseRequest courseRequest) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    existingCourse.setName(courseRequest.getName());
                    existingCourse.setCourseCode(courseRequest.getCourseCode());
                    // Set other properties accordingly
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return ResponseEntity.ok(mapToResponse(updatedCourse));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping("/delete-courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        return courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
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


 */