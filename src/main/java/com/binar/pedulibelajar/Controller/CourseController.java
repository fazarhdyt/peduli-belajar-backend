package com.binar.pedulibelajar.Controller;

import com.binar.pedulibelajar.dto.Request.CourseRequest;
import com.binar.pedulibelajar.dto.Response.CourseResponse;
import com.binar.pedulibelajar.Service.CourseService;
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
    public ResponseEntity<List<CourseResponse>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.createCourse(courseRequest));
    }

    @PutMapping("/{courseCode}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable String courseCode, @RequestBody CourseRequest courseRequest) {
        return ResponseEntity.ok(courseService.updateCourse(courseCode, courseRequest));
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return ResponseEntity.ok(null);
    }

}
