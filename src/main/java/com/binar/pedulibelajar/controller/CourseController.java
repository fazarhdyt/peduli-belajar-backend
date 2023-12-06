package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.CourseResponse;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "api to get all course")
    public ResponseEntity<Object> getAllCourses(){
        return ResponseData.statusResponse(courseService.getAllCourses(), HttpStatus.OK, "success get all course");
    }

    @GetMapping("/{courseCode}")
    @Operation(summary = "api to get course by courseCode")
    public ResponseEntity<Object> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseData.statusResponse(courseService.getCourseByCourseCode(courseCode), HttpStatus.OK, "success get course");
    }

    @GetMapping("/filter")
    @Operation(summary = "api to get course by filter")
    public ResponseEntity<Page<CourseResponse>> getCoursesByFilters(
            @RequestParam(name = "category", required = false) List<String> category,
            @RequestParam(name = "levels", required = false) List<String> levels,
            @RequestParam(name = "types", required = false) List<String> types,
            Pageable pageable) {
        Page<CourseResponse> courses = courseService.getCourseByFilters(category, levels, types, pageable);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/admin")
    @Operation(summary = "api to create course")
    public ResponseEntity<Object> createCourse(@RequestBody CourseRequest courseRequest) {
        return ResponseData.statusResponse(courseService.createCourse(courseRequest), HttpStatus.OK, "success create course");
    }

    @PutMapping("/admin/{courseCode}")
    @Operation(summary = "api to update course")
    public ResponseEntity<Object> updateCourse(@PathVariable String courseCode, @RequestBody CourseRequest courseRequest) {
        return ResponseData.statusResponse(courseService.updateCourse(courseCode, courseRequest), HttpStatus.OK, "success update course");
    }

    @DeleteMapping("/admin/{courseCode}")
    @Operation(summary = "api to delete course")
    public ResponseEntity<Object> deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success delete course");
    }
}