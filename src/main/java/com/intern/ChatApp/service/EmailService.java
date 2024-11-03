package com.intern.ChatApp.service;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String verificationUrl);
}
