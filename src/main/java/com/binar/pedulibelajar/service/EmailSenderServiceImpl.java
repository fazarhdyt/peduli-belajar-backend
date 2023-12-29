package com.binar.pedulibelajar.service;

import com.binar.pedulibelajar.model.OTP;
import com.binar.pedulibelajar.model.Order;
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
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    private SimpleMailMessage message = new SimpleMailMessage();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    private ZonedDateTime zonedDateTime;

    @Override
    public void sendMailOtp(String toEmail, OTP otp) {

        zonedDateTime = otp.getExpiryDate().atZone(ZoneId.systemDefault());

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

        zonedDateTime = token.getExpiryDate().atZone(ZoneId.systemDefault());

        String subject = "Reset Password";
        String body = "Your link to reset password : https://peduli-belajar.vercel.app/resetPassword/" + token.getToken()
                + " valid until " + formatter.format(zonedDateTime);

        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        log.info("mail sent successfully");
    }

    @Override
    public void sendMailOrderSuccess(String toEmail, Order order) {

        String subject = "Order Success";
        String body = "Your order was complete\n" +
                "Detail order :\n" +
                "Order id : " + order.getId() + "\n" +
                "Course Title : " + order.getCourse().getTitle() + "\n" +
                "Payment Method: " + order.getPaymentMethod().toString() + "\n\n" +
                "Join Telegram Group : " + order.getCourse().getTelegramLink();

        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        log.info("mail sent successfully");
    }
}
