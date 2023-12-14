package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Slf4j
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

    @PutMapping("/user")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(
            @ModelAttribute EditProfileRequest editProfileRequest) {
        if (editProfileRequest != null) {
            // log.info("File name: {}",
            // editProfileRequest.getProfilPicture().getOriginalFilename());
            if (!editProfileRequest.getProfilePicture().isEmpty()) {
                userService.editProfile(editProfileRequest);
            } else {
                log.warn("Empty file");
            }
        }
        User updatedUser = userService.editProfile(editProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/user/progress")
    public ResponseEntity<Object> progressUser(@RequestParam String courseCode, @RequestParam String subjectId) {
        return ResponseData.statusResponse(userService.progressUser(courseCode, subjectId), HttpStatus.OK, "success");
    }

    @GetMapping("/admin/activeUser")
    public ResponseEntity<Object> getActiveUser() {
        return ResponseData.statusResponse(userService.getActiveUser(), HttpStatus.OK, "success get active user");
    }
}