package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.IntrospectRequest;
import com.intern.ChatApp.dto.request.LoginRequest;
import com.intern.ChatApp.dto.request.LogoutRequest;
import com.intern.ChatApp.dto.request.RegisterRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.AuthenticationResponse;
import com.intern.ChatApp.dto.response.IntrospectResponse;
import com.intern.ChatApp.dto.response.UserResponse;

public interface AuthService {
    UserResponse registerUser(RegisterRequest request);
    ApiResponse<String> verifyUser(String email);
    AuthenticationResponse authenticateUser(LoginRequest loginRequestDTO);
    void logout(LogoutRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
}
