package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.OrderDetailCourseResponse;
import com.binar.pedulibelajar.dto.response.PaymentHistoryResponse;
import com.binar.pedulibelajar.dto.response.StatusOrderResponse;

import java.util.List;

public interface OrderService {

    void order(OrderRequest orderRequest);

    List<StatusOrderResponse> getStatusOrders();

    List<PaymentHistoryResponse> getPaymentHistory();

    OrderDetailCourseResponse getOrderDetailCourse(String courseCode);
}
