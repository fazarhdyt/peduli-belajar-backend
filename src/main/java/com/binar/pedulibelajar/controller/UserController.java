package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.dto.request.UploadImageRequest;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.CloudinaryService;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PutMapping("/{email}")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(@PathVariable String email,
                                            @RequestBody EditProfileRequest editProfileRequest) {
        User updatedUser = userService.editProfile(email, editProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping(value = "/upload-image")
    @Operation(summary = "api to upload image(profile picture)")
    public String uploadImage(@ModelAttribute UploadImageRequest request) throws IOException {
        log.info("file name : {}", request.getFileName());
        return Optional.ofNullable(request)
                .map(UploadImageRequest::getMultipartFile)
                .filter(file -> !file.isEmpty())
                .map(file -> cloudinaryService.upload(file))
                .orElse("Upload failed");
    }

}