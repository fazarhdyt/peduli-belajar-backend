package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.UploadImageRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.CloudinaryService;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/user/reset-password/request")
    @Operation(summary = "api for user/admin to request reset password")
    public ResponseEntity<Object> requestResetPassword(@RequestParam String email) {

        userService.generateLinkResetPassword(email);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success request reset password");
    }

    @PostMapping("/user/reset-password")
    @Operation(summary = "api for user/admin to reset password")
    public ResponseEntity<Object> resetPassword(@RequestParam String token,
                                                @RequestBody ResetPasswordRequest resetPasswordRequest) {

        userService.resetPassword(token, resetPasswordRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success reset password");
    }

    @PutMapping("/user/{email}")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(@PathVariable String email,
                                            @RequestBody EditProfileRequest editProfileRequest) {
        User updatedUser = userService.editProfile(email, editProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/user/upload-image")
    public String uploadImage(@ModelAttribute UploadImageRequest request) throws IOException{
        log.info("uploader name : {}", request.getUploaderName());
        log.info("file name : {}", request.getFileName());
        return Optional.ofNullable(request)
                .map(UploadImageRequest::getMultipartFile)
                .filter(file -> !file.isEmpty())
                .map(file -> cloudinaryService.upload(file))
                .orElse("Upload failed");
    }

    @PostMapping("/user/progress")
    public ResponseEntity<Object> progressUser(@RequestParam String courseCode, @RequestParam String subjectId) {
        userService.progressUser(courseCode, subjectId);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success");
    }

    @GetMapping("/admin/activeUser")
    public ResponseEntity<Object> getActiveUser() {
        return ResponseData.statusResponse(userService.getActiveUser(), HttpStatus.OK, "success get active user");
    }
}