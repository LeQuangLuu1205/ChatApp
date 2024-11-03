package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.IntrospectRequest;
import com.intern.ChatApp.dto.request.LoginRequest;
import com.intern.ChatApp.dto.request.LogoutRequest;
import com.intern.ChatApp.dto.request.RegisterRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.AuthenticationResponse;
import com.intern.ChatApp.dto.response.IntrospectResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ApiResponse<UserResponse> registerUser(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(authService.registerUser(request))
                .build();
    }

    @GetMapping("/verify")
    public ApiResponse<String> verifyUser(@RequestParam String email) {
        return authService.verifyUser(email);
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest request) {
        var result = authService.authenticateUser(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) {
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

}
