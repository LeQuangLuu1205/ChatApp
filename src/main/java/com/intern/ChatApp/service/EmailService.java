package com.intern.ChatApp.service;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String verificationUrl);
    void sendEmail(String to, String subject, String content);
}
