package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.OrderDetailCourseResponse;
import com.binar.pedulibelajar.dto.response.PaymentHistoryResponse;
import com.binar.pedulibelajar.dto.response.StatusOrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, String> orderPremium(OrderRequest orderRequest);

    void orderFree(String courseCode);

    void resolveOrder(String orderId);

    List<StatusOrderResponse> getStatusOrders();

    List<PaymentHistoryResponse> getPaymentHistory();

    OrderDetailCourseResponse getOrderDetailCourse(String courseCode);
}
