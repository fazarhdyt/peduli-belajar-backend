package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.User;

public interface UserService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    User registerUser(SignupRequest signupRequest);

    User registerAdmin(SignupRequest signupRequest);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    User editProfile(String email, EditProfileRequest editProfileRequest);

    void updatePassword(); //bikin dto request untuk update password
}
