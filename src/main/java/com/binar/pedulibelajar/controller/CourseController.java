package com.binar.pedulibelajar.controller;


import com.binar.pedulibelajar.model.Course;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping(value = "/tambah-course")
    public ResponseEntity<?> tambahKelas(@RequestBody Course course) {
        courseService.tambahCourse(course);
        return new ResponseEntity<>("Tambah kelas berhasil", HttpStatus.OK);
    }
}