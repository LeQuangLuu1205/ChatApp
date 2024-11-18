package com.intern.ChatApp.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;        // Token được gửi qua email
    private String newPassword;  // Mật khẩu mới
}