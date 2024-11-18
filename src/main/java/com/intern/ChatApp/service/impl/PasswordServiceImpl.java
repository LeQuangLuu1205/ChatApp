package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.entity.ResetToken;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.ResetTokenRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.EmailService;
import com.intern.ChatApp.service.PasswordService;
import org.apache.kafka.common.errors.ApiException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        String resetTokenValue = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken();
        resetToken.setUser(user);
        resetToken.setToken(resetTokenValue);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(5));
        resetTokenRepository.save(resetToken);

        String resetLink = "http://127.0.0.1:5500/reset-password.html?token=" + resetTokenValue;
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "Click the link to reset your password: " + resetLink
        );

        return "Reset link has been sent to email: " + email;
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetTokenRepository.deleteByToken(token);
            throw new IllegalArgumentException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.delete(resetToken);

        return  "Password reset successfully for user: " + user.getEmail();
    }

}
