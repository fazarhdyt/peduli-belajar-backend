package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.UpdatePasswordRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/reset-password/request")
    @Operation(summary = "api for user/admin to request reset password")
    public ResponseEntity<Object> requestResetPassword(@RequestParam String email) {

        userService.generateLinkResetPassword(email);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success request reset password");
    }

    @PostMapping("/user/reset-password/{token}")
    @Operation(summary = "api for user/admin to reset password")
    public ResponseEntity<Object> resetPassword(@PathVariable String token,
            @RequestBody ResetPasswordRequest resetPasswordRequest) {

        userService.resetPassword(token, resetPasswordRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success reset password");
    }

    @GetMapping("/user")
    @Operation(summary = "api to get user profile")
    public ResponseEntity<Object> getUser() {
        return ResponseData.statusResponse(userService.getUser(), HttpStatus.OK, "success get user profile");
    }

    @PutMapping("user/{email}")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(
            @ModelAttribute EditProfileRequest editProfileRequest) {
        if (editProfileRequest != null) {
            try {
                User updatedUser = userService.editProfile(editProfileRequest);
                return ResponseEntity.ok(updatedUser);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/user/updatePassword")
    @Operation(summary = "api to user update password")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(updatePasswordRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success update password");
    }

    @PostMapping("/user/progress")
    @Operation(summary = "api for calculate learning progress user")
    public ResponseEntity<Object> progressUser(@RequestParam String courseCode, @RequestParam String subjectId) {
        return ResponseData.statusResponse(userService.progressUser(courseCode, subjectId), HttpStatus.OK, "success");
    }

    @GetMapping("/admin/activeUser")
    @Operation(summary = "api for admin to get active user")
    public ResponseEntity<Object> getActiveUser() {
        return ResponseData.statusResponse(userService.getActiveUser(), HttpStatus.OK, "success get active user");
    }
}