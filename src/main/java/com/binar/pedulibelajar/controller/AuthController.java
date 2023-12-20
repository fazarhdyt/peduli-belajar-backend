package com.binar.pedulibelajar.controller;

import com.binar.pedulibelajar.dto.request.LoginRequest;
import com.binar.pedulibelajar.dto.request.SignupRequest;
import com.binar.pedulibelajar.dto.response.ResponseData;
import com.binar.pedulibelajar.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "api for user to register")
    public ResponseEntity<Object> registerUser(@RequestBody SignupRequest signupRequest) {
        authService.registerUser(signupRequest);
        return ResponseData.statusResponse(null, HttpStatus.CREATED, "User registered successfully!");
    }

    @PostMapping("/signup/admin")
    @Operation(summary = "api for admin to register")
    public ResponseEntity<Object> registerAdmin(@RequestBody SignupRequest signupRequest) {

        return ResponseData.statusResponse(authService.registerAdmin(signupRequest), HttpStatus.CREATED,
                "User registered successfully!");
    }

    @PostMapping("/signin")
    @Operation(summary = "api for user/admin to login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        return ResponseData.statusResponse(authService.authenticateUser(loginRequest, response), HttpStatus.OK,
                "user login successfully!");
    }

    @PostMapping("/verify")
    @Operation(summary = "api for user/admin to verify account with otp code")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestParam String otp) {

        authService.verifyAccount(email, otp);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success verify account");
    }

    @PutMapping("/regenerate-otp")
    @Operation(summary = "api for user/admin to regenerate otp code")
    public ResponseEntity<Object> regenerateOtp(@RequestParam String email) {

        authService.regenerateOtp(email);
        return ResponseData.statusResponse(null, HttpStatus.OK, "success generate otp");
    }
}