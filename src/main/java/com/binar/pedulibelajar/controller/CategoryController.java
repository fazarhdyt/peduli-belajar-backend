package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.response.CategoryResponse;
import com.binar.pedulibelajar.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "api to get categories")
    public ResponseEntity<List<CategoryResponse>> gCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

}