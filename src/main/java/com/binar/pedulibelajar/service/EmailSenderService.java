package com.binar.pedulibelajar.service;

public interface EmailSenderService {

    void sendMail(String toEmail, String subject, String body);
}
