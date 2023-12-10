package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.PaymentHistoryResponse;
import com.binar.pedulibelajar.dto.response.StatusOrderResponse;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.OrderRepository;
import com.binar.pedulibelajar.repository.UserCourseRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Override
    public void order(OrderRequest orderRequest) {

        if (orderRepository.existsUserOrder(orderRequest.getEmail(), orderRequest.getCourseCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you have purchased this course");
        }

        Order order = Order.builder()
                .user(userRepository.findByEmail(orderRequest.getEmail())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")))
                .course(courseRepository.findByCourseCode(orderRequest.getCourseCode())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found")))
                .paymentMethod(orderRequest.getPaymentMethod())
                .paymentDate(new Date())
                .paid(true)
                .build();

        orderRepository.save(order);
        storeToUserCourse(order);

    }

    @Override
    public List<StatusOrderResponse> getStatusOrders() {

        return orderRepository.findAll().stream()
                .filter(order -> order.getCourse().getType().equalsIgnoreCase("PREMIUM"))
                .map(this::mapToStatusOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentHistoryResponse> getPaymentHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return user.getOrder().stream()
                .map(this::mapToPaymentHistoryResponse)
                .collect(Collectors.toList());
    }

    private StatusOrderResponse mapToStatusOrderResponse(Order order) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StatusOrderResponse response = new StatusOrderResponse();
        response.setUsername(order.getUser().getFullName());
        response.setCategory(order.getCourse().getCategory());
        response.setTitle(order.getCourse().getTitle());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setStatus(order.isPaid() ? "SUDAH BAYAR" : "BELUM BAYAR");
        response.setPaymentDate(fmt.format(order.getPaymentDate()));
        return response;
    }

    private PaymentHistoryResponse mapToPaymentHistoryResponse(Order order) {
        PaymentHistoryResponse response = new PaymentHistoryResponse();
        response.setCategory(order.getCourse().getCategory());
        response.setTitle(order.getCourse().getTitle());
        response.setTeacher(order.getCourse().getTeacher());
        response.setLevel(order.getCourse().getLevel());
        response.setStatus(order.isPaid() ? "Paid" : "Waiting for Payment");
        return response;
    }

    private void storeToUserCourse(Order order) {

        for (Chapter chapter : order.getCourse().getChapter()) {
            for (Subject subject : chapter.getSubject()) {
                UserCourse userCourse = new UserCourse();
                userCourse.setCourse(order.getCourse());
                userCourse.setUser(order.getUser());
                userCourse.setChapter(chapter.getChapterTitle());
                userCourse.setSubject(subject.getVideoTitle());
                userCourseRepository.save(userCourse);
            }
        }
    }
}
