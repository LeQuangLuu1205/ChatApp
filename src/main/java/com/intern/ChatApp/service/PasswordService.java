package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.response.ApiResponse;

public interface PasswordService {
    String initiatePasswordReset(String email);

    ApiResponse<String> resetPassword(String token, String newPassword);
}
