package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.SubjectRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/admin/subject/{id}")
    @Operation(summary = "api to get subject")
    public ResponseEntity<Object> getChapter(@PathVariable String id) {
        return ResponseData.statusResponse(subjectService.getSubject(id), HttpStatus.OK,
                "success get subject");
    }

    @PostMapping("/admin/subject")
    @Operation(summary = "api to create subject")
    public ResponseEntity<Object> createCourse(@RequestParam String chapterId,
                                               @RequestBody SubjectRequest subjectRequest) {
        return ResponseData.statusResponse(subjectService.createSubject(chapterId, subjectRequest), HttpStatus.OK,
                "success create subject");
    }

    @PutMapping("/admin/subject/{id}")
    @Operation(summary = "api to update subject")
    public ResponseEntity<Object> updateCourse(@PathVariable String id,
                                               @RequestBody SubjectRequest subjectRequest) {
        return ResponseData.statusResponse(subjectService.updateSubject(id, subjectRequest), HttpStatus.OK,
                "success update subject");
    }
}
