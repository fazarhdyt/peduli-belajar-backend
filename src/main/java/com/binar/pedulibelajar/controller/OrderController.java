package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    @Operation(summary = "api to user order course")
    public ResponseEntity<Object> orderCourse(@RequestBody OrderRequest orderRequest) {
        orderService.order(orderRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success order course");
    }

    @GetMapping("/order/payment-history/{email}")
    @Operation(summary = "api to user get payment history")
    public ResponseEntity<Object> getPaymentHistory(@PathVariable String email) {
        return ResponseData.statusResponse(orderService.getPaymentHistory(email), HttpStatus.OK, "success get payment history");
    }

    @GetMapping("/admin/order/status-order")
    @Operation(summary = "api to admin get status order user")
    public ResponseEntity<Object> getStatusOrder() {
        return ResponseData.statusResponse(orderService.getStatusOrders(), HttpStatus.OK, "success get status order course");
    }
}
