package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.UpdatePasswordRequest;
import com.binar.pedulibelajar.dto.response.UserResponse;

import java.util.Map;

public interface UserService {

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    UserResponse getUser();

    UserResponse editProfile(EditProfileRequest editProfileRequest);

    Map<String, Double> progressUser(String courseCode, String subjectId);

    long getActiveUser();

    void updatePassword(UpdatePasswordRequest updatePasswordDTO);

}