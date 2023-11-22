package com.binar.pedulibelajar.Controller;

import com.binar.pedulibelajar.Model.Request.CourseRequest;
import com.binar.pedulibelajar.Model.Response.CourseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CourseController {
    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> getAllCourse();
    @GetMapping("/courses/{id}")
    ResponseEntity<CourseResponse> getCourseById(@PathVariable String id);
    @PostMapping("/course")
    ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest);
    @PutMapping("/course/{id}")
    ResponseEntity<CourseResponse> updateCourse(@PathVariable String id, @RequestBody CourseRequest courseRequest);
    @DeleteMapping("/courses/{id}")
    ResponseEntity<Void> deleteCourse(@PathVariable String id);
}
