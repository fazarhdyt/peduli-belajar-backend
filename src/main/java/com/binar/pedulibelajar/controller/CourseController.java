package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.CourseRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.enumeration.CourseCategory;
import com.binar.pedulibelajar.enumeration.CourseLevel;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/course")
    @Operation(summary = "api to get all course")
    public ResponseEntity<Object> getAllCourses() {
        return ResponseData.statusResponse(courseService.getAllCourses(), HttpStatus.OK, "success get all course");
    }

    @GetMapping("/course/{courseCode}")
    @Operation(summary = "api to get course by courseCode")
    public ResponseEntity<Object> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseData.statusResponse(courseService.getCourseByCourseCode(courseCode), HttpStatus.OK,
                "success get course");
    }

    @GetMapping("/course/filter")
    @Operation(summary = "api to get course by filter")
    public ResponseEntity<Object> getCoursesByFilters(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "3") Integer size,
            @RequestParam(name = "category", required = false) List<CourseCategory> category,
            @RequestParam(name = "level", required = false) List<CourseLevel> level,
            @RequestParam(name = "type", required = false) List<Type> type,
            @RequestParam(required = false) String title) {

        return ResponseData.statusResponse(courseService.getCourseByFilters(page, size, category, level, type, title),
                HttpStatus.OK, "success get courses");
    }

    @GetMapping("/course/my-course")
    @Operation(summary = "api to get course by filter")
    public ResponseEntity<Object> getMyCoursesByFilters(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "3") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<CourseCategory> category,
            @RequestParam(required = false) List<CourseLevel> level,
            @RequestParam(required = false) List<Type> type) {

        return ResponseData.statusResponse(
                courseService.getMyCourse(page, size, category, level, type, title), HttpStatus.OK,
                "success get my courses");
    }

    @GetMapping("/admin/totalCourse")
    @Operation(summary = "api to get total courses")
    public ResponseEntity<Object> getTotalCourses() {
        return ResponseData.statusResponse(courseService.getTotalCourse(), HttpStatus.OK, "success get total course");
    }

    @GetMapping("/admin/totalPremiumCourse")
    @Operation(summary = "api to get total premium courses")
    public ResponseEntity<Object> getTotalPremiumCourses() {
        return ResponseData.statusResponse(courseService.getPremiumCourse(), HttpStatus.OK, "success get premium course");
    }

    @PostMapping("/admin/course")
    @Operation(summary = "api to create course")
    public ResponseEntity<Object> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        return ResponseData.statusResponse(courseService.createCourse(courseRequest), HttpStatus.OK,
                "success create course");
    }

    @PutMapping("/admin/course/{courseCode}")
    @Operation(summary = "api to update course")
    public ResponseEntity<Object> updateCourse(@PathVariable String courseCode,
            @RequestBody CourseRequest courseRequest) {
        return ResponseData.statusResponse(courseService.updateCourse(courseCode, courseRequest), HttpStatus.OK,
                "success update course");
    }

    @DeleteMapping("/admin/course/{courseCode}")
    @Operation(summary = "api to delete course")
    public ResponseEntity<Object> deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success delete course");
    }
}