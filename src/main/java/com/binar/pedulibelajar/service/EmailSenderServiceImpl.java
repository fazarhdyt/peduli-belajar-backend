package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.TokenResetPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMailOtp(String toEmail, OTP otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        ZonedDateTime zonedDateTime = otp.getExpiryDate().atZone(ZoneId.systemDefault());

        String subject = "Account Verification";
        String body = "Your OTP : " + otp.getOtp() + " valid until " + formatter.format(zonedDateTime);

        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        log.info("mail sent successfully");
    }

    @Override
    public void sendMailLinkResetPassword(String toEmail, TokenResetPassword token) {
        SimpleMailMessage message = new SimpleMailMessage();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        ZonedDateTime zonedDateTime = token.getExpiryDate().atZone(ZoneId.systemDefault());

        String subject = "Reset Password";
        String body = "Your link to reset password : localhost:3000/reset-password?token=" + token.getToken() + " valid until " + formatter.format(zonedDateTime);

        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        log.info("mail sent successfully");
    }
}
