package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.OrderRequest;
import com.binar.pedulibelajar.model.Order;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.OrderRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void order(OrderRequest orderRequest) {

        if(orderRepository.existsUserOrder(orderRequest.getEmail(), orderRequest.getCourseCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you have purchased this course");
        }

        Order order = Order.builder()
                .user(userRepository.findByEmail(orderRequest.getEmail())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")))
//                .course(courseRepository.findByCourseCode(orderRequest.getCourseCode())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "course not found")))
                .paymentMethod(orderRequest.getPaymentMethod())
                .paid(true)
                .build();

        orderRepository.save(order);
    }
}
