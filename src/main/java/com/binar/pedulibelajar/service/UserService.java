package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    User registerUser(SignupRequest signupRequest);

    User registerAdmin(SignupRequest signupRequest);

    void verifyAccount(String email, String otp);

    User editProfile(EditProfileRequest editProfileRequest);
}