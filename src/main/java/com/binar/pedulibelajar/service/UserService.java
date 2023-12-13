package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.model.User;

public interface UserService {

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    User editProfile(String email, EditProfileRequest editProfileRequest);

    void progressUser(String courseCode, String subjectId);

    long getActiveUser();
}
