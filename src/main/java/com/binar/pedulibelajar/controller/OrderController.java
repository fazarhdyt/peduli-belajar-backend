package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "api to user order course")
    public ResponseEntity<Object> orderCourse(@RequestBody OrderRequest orderRequest) {
        orderService.order(orderRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success order course");
    }
}
