package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.enumeration.ERole;
import com.binar.pedulibelajar.model.*;
import com.binar.pedulibelajar.repository.*;
import com.binar.pedulibelajar.security.jwt.JwtUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.io.IOException;
import java.util.Map;

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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailSenderServiceImpl senderService;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private Cloudinary cloudinary;

    private TokenResetPasswordService resetPasswordService;

    @Autowired
    private TokenResetPasswordRepository tokenResetPasswordRepository;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "account has not been verified");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
        jwtCookie.setMaxAge(3600);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return new JwtResponse(jwt,
                userDetails.getEmail(),
                userDetails.getNoTelp(),
                userDetails.getRole().toString());
    }

    @Override
    @Async
    public User registerUser(SignupRequest signupRequest) {
        boolean userExist = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExist) {
            throw new RuntimeException(
                    String.format("user with email '%s' already exist", signupRequest.getEmail()));
        }

        User user = modelMapper.map(signupRequest, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(ERole.USER);

        userRepository.save(user);

        OTP otp = otpService.createOTP(user.getEmail());

        senderService.sendMailOtp(user.getEmail(), otp);

        return user;
    }

    @Override
    @Async
    public User registerAdmin(SignupRequest signupRequest) {
        boolean userExist = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExist) {
            throw new RuntimeException(
                    String.format("user with email '%s' already exist", signupRequest.getEmail()));
        }

        User user = modelMapper.map(signupRequest, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(ERole.ADMIN);

        userRepository.save(user);

        OTP otp = otpService.createOTP(user.getEmail());

        senderService.sendMailOtp(user.getEmail(), otp);

        return user;
    }

    @Override
    public void verifyAccount(String email, String otp) {

        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        if (!otpRepository.existsByOtp(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP code");
        }

        otpService.findByOtp(otp)
                .map(otpService::verifyExpiration)
                .map(OTP::getUser)
                .ifPresent(user -> {
                    user.setActive(true);
                    userRepository.save(user);
                    otpService.deleteByEmail(user.getEmail());
                });
    }

    @Override
    public void regenerateOtp(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "email not found"));
        OTP otp = otpService.createOTP(user.getEmail());

        senderService.sendMailOtp(user.getEmail(), otp);
    }

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
    public User editProfile(EditProfileRequest editProfileRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        modelMapper.map(editProfileRequest, existingUser);

        if (editProfileRequest != null && !editProfileRequest.getProfilePicture().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(editProfileRequest.getProfilePicture().getBytes(),
                        ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                existingUser.setProfilePictureUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userRepository.save(existingUser);
    }

    @Override
    public double progressUser(String courseCode, String subjectId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "email not found"));

        UserCourse userCourse = userCourseRepository.findByUserIdAndSubjectId(user.getId(),
                subjectId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));
        userCourse.setDone(true);
        userCourseRepository.save(userCourse);

        return calculatePercentage(user, courseCode);
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
