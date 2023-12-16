package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.dto.request.*;
import com.binar.pedulibelajar.model.User;

public interface UserService {

    void generateLinkResetPassword(String email);

    void resetPassword(String token, ResetPasswordRequest resetPasswordRequest);

    User editProfile(EditProfileRequest editProfileRequest);

    double progressUser(String courseCode, String subjectId);

    long getActiveUser();

    void updatePassword(UpdatePasswordRequest updatePasswordDTO);
}