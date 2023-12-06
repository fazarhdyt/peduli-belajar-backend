package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.OTP;

import java.util.Optional;

public interface OTPService {

    Optional<OTP> findByOtp(String otp);

    OTP createOTP(String email);

    OTP verifyExpiration(OTP otp);

    void deleteByEmail(String email);

}