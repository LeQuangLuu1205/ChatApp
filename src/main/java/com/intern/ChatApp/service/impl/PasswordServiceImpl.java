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

    @Override
    public String initiatePasswordReset(String email) {
        // 1. Tìm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        // 2. Tạo token reset mật khẩu
        String resetTokenValue = UUID.randomUUID().toString();
        ResetToken resetToken = new ResetToken();
        resetToken.setUser(user);
        resetToken.setToken(resetTokenValue);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        resetTokenRepository.save(resetToken);

        // 3. Gửi email với đường dẫn reset
        String resetLink = "https://your-app.com/reset-password?token=" + resetTokenValue;
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "Click the link to reset your password: " + resetLink
        );

        return "Reset link has been sent to email: " + email;
    }


    @Override
    public ApiResponse<String> resetPassword(String token, String newPassword) {
        return null;
    }
}
