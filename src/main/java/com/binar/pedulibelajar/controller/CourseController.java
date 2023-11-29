package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.Request.CourseRequest;
import com.binar.pedulibelajar.dto.Response.CourseResponse;
import com.binar.pedulibelajar.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    @Operation(summary = "api to get course")
    public ResponseEntity<List<CourseResponse>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{courseCode}")
    @Operation(summary = "api to get course by courseCode")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @PostMapping
    @Operation(summary = "api to create course")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.createCourse(courseRequest));
    }

    @PutMapping("/{courseCode}")
    @Operation(summary = "api to update course")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable String courseCode, @RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.updateCourse(courseCode, courseRequest));
    }

    @DeleteMapping("/{courseCode}")
    @Operation(summary = "api to delete course")
    public ResponseEntity deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return ResponseEntity.ok(null);
    }
}
