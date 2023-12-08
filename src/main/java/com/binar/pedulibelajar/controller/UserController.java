package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.EditProfileRequest;
import com.binar.pedulibelajar.model.User;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{email}")
    @Operation(summary = "api to edit profile")
    public ResponseEntity<User> editProfile(@PathVariable String email,
                                            @RequestBody EditProfileRequest editProfileRequest) {
        User updatedUser = userService.editProfile(email, editProfileRequest);
        return ResponseEntity.ok(updatedUser);
    }
}