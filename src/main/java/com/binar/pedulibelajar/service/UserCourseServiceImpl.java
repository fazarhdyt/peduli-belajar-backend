package com.binar.pedulibelajar.service;
import java.util.Optional;

import com.binar.pedulibelajar.model.Course;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.model.UserCourse;
import com.binar.pedulibelajar.repository.CourseRepository;
import com.binar.pedulibelajar.repository.OrderRepository;
import com.binar.pedulibelajar.repository.UserCourseRepository;
import com.binar.pedulibelajar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean hasUserPurchasedCourse(String userEmail, String courseCode) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Course> course = courseRepository.findByCourseCode(courseCode);
        if (course.isPresent()) {
            UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course.get()).stream()
                    .findFirst().orElse(null);
            if(!orderRepository.isPaidByUserAndCourse(user, course.get())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "complete the payment first");
            }
            return userCourse != null;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
    }
}
