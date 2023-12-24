package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.Order;
import com.binar.pedulibelajar.model.TokenResetPassword;

public interface EmailSenderService {

    void sendMailOtp(String toEmail, OTP otp);

    void sendMailLinkResetPassword(String toEmail, TokenResetPassword token);

    void sendMailOrderSuccess(String toEmail, Order order);

}
