package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.dto.response.OrderDetailCourseResponse;
import com.binar.pedulibelajar.dto.response.PaymentHistoryResponse;
import com.binar.pedulibelajar.dto.response.StatusOrderResponse;
import com.binar.pedulibelajar.enumeration.Type;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.OrderRepository;
import com.binar.pedulibelajar.repository.UserCourseRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (orderRepository.existsUserOrder(email, orderRequest.getCourseCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you have purchased this course");
        }

        Order order = Order.builder()
                .user(userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")))
                .course(courseRepository.findByCourseCode(orderRequest.getCourseCode())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found")))
                .paymentMethod(orderRequest.getPaymentMethod())
                .paymentDate(new Date())
                .paid(false)
                .build();

        orderRepository.save(order);
        storeToUserCourse(order);

    }

    @Override
    public List<StatusOrderResponse> getStatusOrders() {

        return orderRepository.findAll().stream()
                .filter(order -> order.getCourse().getType().equals(Type.PREMIUM))
                .map(this::mapToStatusOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentHistoryResponse> getPaymentHistory() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        return user.getOrder().stream()
                .map(this::mapToPaymentHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailCourseResponse getOrderDetailCourse(String courseCode) {

        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found"));

        final double tax = 0.11;
        double price = course.getPrice();
        double calculateTax = tax * price;
        double totalPrice = price + (price * tax);

        return OrderDetailCourseResponse.builder()
                .courseTitle(course.getTitle())
                .category(course.getCategory())
                .authorCourse(course.getTeacher())
                .price(price)
                .tax(calculateTax)
                .totalPrice(totalPrice)
                .build();
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
                userCourse.setChapterId(chapter.getId());
                userCourse.setSubjectId(subject.getId());
                userCourseRepository.save(userCourse);
            }
        }
    }
}
