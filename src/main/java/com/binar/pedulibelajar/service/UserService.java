package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.UpdatePasswordRequest;
import com.binar.pedulibelajar.dto.response.UserResponse;
import com.binar.pedulibelajar.model.User;

public interface UserService {

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    UserResponse getUser();

    User editProfile(EditProfileRequest editProfileRequest);

    double progressUser(String courseCode, String subjectId);

    long getActiveUser();

    void updatePassword(UpdatePasswordRequest updatePasswordDTO);

}