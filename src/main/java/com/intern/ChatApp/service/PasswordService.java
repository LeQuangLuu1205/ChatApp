package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.ChangePasswordRequest;
import com.intern.ChatApp.dto.response.ApiResponse;

public interface PasswordService {
    String initiatePasswordReset(String email);
    String resetPassword(String token, String newPassword);
    void changePassword(ChangePasswordRequest request);
}
