package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.*;
import com.binar.pedulibelajar.dto.response.JwtResponse;
import com.binar.pedulibelajar.model.User;

public interface UserService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    void registerUser(SignupRequest signupRequest);

    void registerAdmin(SignupRequest signupRequest);

    void verifyAccount(String email, String otp);

    void regenerateOtp(String email);

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    User editProfile(String email, EditProfileRequest editProfileRequest);

    void updatePassword(UpdatePasswordDTO updatePasswordDTO);
}