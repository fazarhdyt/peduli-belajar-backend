package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.User;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response);

    User registerUser(SignupRequest signupRequest);

    User registerAdmin(SignupRequest signupRequest);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);
}
