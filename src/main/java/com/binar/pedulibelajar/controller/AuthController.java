package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.ResetPasswordRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "api for user to register")
    public ResponseEntity<Object> registerUser(@RequestBody SignupRequest signupRequest) {
        userService.registerUser(signupRequest);
        return ResponseData.statusResponse(null, HttpStatus.CREATED, "User registered successfully!");
    }


    @PostMapping("/signin")
    @Operation(summary = "api for user/admin to login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {

        return ResponseData.statusResponse(userService.authenticateUser(loginRequest), HttpStatus.OK,
                "user login successfully!");
    }

    @PostMapping("/verify")
    @Operation(summary = "api for user/admin to verify account with otp code")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestParam String otp) {

        userService.verifyAccount(email, otp);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success verify account");
    }

    @PutMapping("/regenerate-otp")
    @Operation(summary = "api for user/admin to regenerate otp code")
    public ResponseEntity<Object> regenerateOtp(@RequestParam String email) {

        userService.regenerateOtp(email);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success generate otp");
    }

    @PostMapping("/reset-password/request")
    @Operation(summary = "api for user/admin to request reset password")
    public ResponseEntity<Object> requestResetPassword(@RequestParam String email) {

        userService.generateLinkResetPassword(email);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success request reset password");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "api for user/admin to reset password")
    public ResponseEntity<Object> resetPassword(@RequestParam String token,
                                                @RequestBody ResetPasswordRequest resetPasswordRequest) {

        userService.resetPassword(token, resetPasswordRequest);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success reset password");
    }
}