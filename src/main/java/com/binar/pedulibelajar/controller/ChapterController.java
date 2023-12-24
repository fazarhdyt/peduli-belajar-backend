package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.ChapterRequest;
import com.binar.pedulibelajar.dto.request.EditChapterRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/admin/chapter/{id}")
    @Operation(summary = "api to get chapter")
    public ResponseEntity<Object> getChapter(@PathVariable String id) {
        return ResponseData.statusResponse(chapterService.getChapter(id), HttpStatus.OK,
                "success get chapter");
    }

    @PostMapping("/admin/chapter")
    @Operation(summary = "api to create chapter")
    public ResponseEntity<Object> createCourse(@RequestParam String courseId,
                                               @RequestBody ChapterRequest chapterRequest) {
        return ResponseData.statusResponse(chapterService.createChapter(courseId, chapterRequest), HttpStatus.OK,
                "success create chapter");
    }

    @PutMapping("/admin/chapter/{id}")
    @Operation(summary = "api to update chapter")
    public ResponseEntity<Object> updateCourse(@PathVariable String id,
                                               @RequestBody EditChapterRequest editChapterRequest) {
        return ResponseData.statusResponse(chapterService.updateChapter(id, editChapterRequest), HttpStatus.OK,
                "success update chapter");
    }
}
