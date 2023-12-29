package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.dto.response.StatusOrderResponse;
import com.binar.pedulibelajar.service.OrderService;
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
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    @Operation(summary = "api for user to order course")
    public ResponseEntity<Object> orderCourse(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseData.statusResponse(orderService.orderPremium(orderRequest), HttpStatus.OK,
                "success order course");
    }

    @PutMapping("/order/resolve")
    @Operation(summary = "api to resolve order")
    public ResponseEntity<Object> resolveOrder(@RequestParam String orderId) {
        orderService.resolveOrder(orderId);
        return ResponseData.statusResponse(null, HttpStatus.OK, "successfully completed the order");
    }

    @GetMapping("/order/{courseCode}")
    @Operation(summary = "api for user to get detail order")
    public ResponseEntity<Object> getOrderDetail(@PathVariable String courseCode) {
        return ResponseData.statusResponse(orderService.getOrderDetailCourse(courseCode), HttpStatus.OK,
                "success get payment history");
    }

    @GetMapping("/order/payment-history")
    @Operation(summary = "api for user to get payment history")
    public ResponseEntity<Object> getPaymentHistory() {
        return ResponseData.statusResponse(orderService.getPaymentHistory(), HttpStatus.OK,
                "success get payment history");
    }

    @GetMapping("admin/status-order")
    @Operation(summary = "api for admin to get status order")
    public List<StatusOrderResponse> getStatusOrders(@RequestParam(required = false) Boolean isPaid, @RequestParam(required = false) String title) {
        return orderService.getStatusOrders(isPaid, title);
    }
}
