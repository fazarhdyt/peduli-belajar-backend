package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{email}")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(
            @ModelAttribute EditProfileRequest editProfileRequest
    ) {
        if (editProfileRequest != null) {
            //log.info("File name: {}", editProfileRequest.getProfilPicture().getOriginalFilename());
            if (!editProfileRequest.getProfilePicture().isEmpty()) {
                userService.editProfile(editProfileRequest);
            } else {
                log.warn("Empty file");
            }
        }
        User updatedUser = userService.editProfile(editProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }
}