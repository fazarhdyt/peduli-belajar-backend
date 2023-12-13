package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailSenderServiceImpl senderService;

    @Autowired
    private TokenResetPasswordService resetPasswordService;

    @Autowired
    private TokenResetPasswordRepository tokenResetPasswordRepository;

    @Override
    @Async
    public void generateLinkResetPassword(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "email not found"));
        TokenResetPassword token = resetPasswordService.createToken(user.getEmail());

        senderService.sendMailLinkResetPassword(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {

        if (!tokenResetPasswordRepository.existsByToken(token)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "link not valid");
        }

        resetPasswordService.findByToken(token)
                .map(resetPasswordService::verifyExpiration)
                .map(TokenResetPassword::getUser)
                .ifPresent(user -> {
                    if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password not match");
                    }
                    String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword());
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    resetPasswordService.deleteByEmail(user.getEmail());
                });
    }

    @Override
    public User editProfile(String email, EditProfileRequest editProfileRequest) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        modelMapper.map(editProfileRequest, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public void progressUser(String courseCode, String subjectId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "email not found"));

        UserCourse userCourse = userCourseRepository.findByUserIdAndSubjectId(user.getId(),
                subjectId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));
        userCourse.setDone(true);
        userCourseRepository.save(userCourse);

        calculatePercentage(user, courseCode);
    }

    @Override
    public long getActiveUser() {
        return userRepository.countActiveUsers();
    }

    private int getTotalSubjectCount(Course course) {
        int totalSubjectCount = 0;

        for (Chapter chapter : course.getChapter()) {
            totalSubjectCount += chapter.getSubject().size();
        }

        return totalSubjectCount;
    }

    private void calculatePercentage(User user, String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<UserCourse> userCourses = userCourseRepository.findByUserAndCourse(user, course);

        if (userCourses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "UserCourse data not found for the given user and course");
        }

        int totalSubjectCount = getTotalSubjectCount(course);

        long completedSubjectCount = userCourses.stream()
                .filter(UserCourse::isDone)
                .count();

        double percent = (double) completedSubjectCount / totalSubjectCount * 100;
        long roundedPercent = Math.round(percent);

        UserProgress userProgress = userProgressRepository.findByUserAndCourse(user, course)
                .orElse(new UserProgress(user, course)); // Create a new UserProgress if not found

        userProgress.setPercent(roundedPercent);

        userProgressRepository.save(userProgress);
    }

}
