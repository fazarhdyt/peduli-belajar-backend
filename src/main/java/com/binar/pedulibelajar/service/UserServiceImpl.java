package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.UpdatePasswordRequest;
import com.binar.pedulibelajar.dto.response.UserResponse;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;

@Service
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
    private Cloudinary cloudinary;

    @Autowired
    private TokenResetPasswordService resetPasswordService;

    @Autowired
    private TokenResetPasswordRepository tokenResetPasswordRepository;

    @Autowired
    private OrderRepository orderRepository;

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
    @Transactional(readOnly = true)
    public UserResponse getUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return modelMapper.map(userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")), UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse editProfile(EditProfileRequest editProfileRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userUpdate = modelMapper.map(editProfileRequest, User.class);

        if (editProfileRequest.getProfilePicture() != null && !editProfileRequest.getProfilePicture().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        editProfileRequest.getProfilePicture().getBytes(),
                        ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                existingUser.setProfilePictureUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        existingUser.setFullName(userUpdate.getFullName().isEmpty() ? existingUser.getFullName() : userUpdate.getFullName());
        existingUser.setNoTelp(userUpdate.getNoTelp().isEmpty() ? existingUser.getNoTelp() : userUpdate.getNoTelp());
        existingUser.setCity(userUpdate.getCity().isEmpty() ? existingUser.getCity() : userUpdate.getCity());
        existingUser.setCountry(userUpdate.getCountry().isEmpty() ? existingUser.getCountry() : userUpdate.getCountry());
        userRepository.save(existingUser);
        return modelMapper.map(existingUser, UserResponse.class);
    }

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        // Mendapatkan pengguna yang saat ini logins
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(currentAuthentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Memvalidasi password lama
        if (!bCryptPasswordEncoder.matches(updatePasswordRequest.getOldPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password is incorrect");
        }

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password not match");
        }

        // Mengganti password lama dengan password baru
        currentUser.setPassword(bCryptPasswordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(currentUser);
    }

    @Override
    @Transactional
    public Map<String, Double> progressUser(String courseCode, String subjectId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "email not found"));

        UserCourse userCourse = userCourseRepository.findByUserIdAndSubjectId(user.getId(),
                subjectId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));
        userCourse.setDone(true);
        userCourseRepository.save(userCourse);
        Map<String, Double> response = new HashMap<>();
        response.put("percent", calculatePercentage(user, courseCode));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public long getActiveUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        return orderRepository.countActiveUsers(user.getFullName());
    }

    private int getTotalSubjectCount(Course course) {
        int totalSubjectCount = 0;

        for (Chapter chapter : course.getChapter()) {
            totalSubjectCount += chapter.getSubject().size();
        }

        return totalSubjectCount;
    }

    private double calculatePercentage(User user, String courseCode) {
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

        return roundedPercent;
    }

}