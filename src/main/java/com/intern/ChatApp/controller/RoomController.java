package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private SecurityUtil securityUtil;
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MODERATOR')")
    public ApiResponse<?> getAllRoom() {
        String email = securityUtil.extractEmailFromSecurityContext();
        return ApiResponse.<String>builder()
                .result(email)
                .message("Email retrieved successfully")
                .build();
    }



}
