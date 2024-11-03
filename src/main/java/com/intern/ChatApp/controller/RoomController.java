package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.response.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MODERATOR')")
    public ApiResponse<?> getAllRoom() {
        return ApiResponse.<String>builder()
                .result("Hello")
                .build();
    }
    @GetMapping("/getdemo")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getDemo() {
        return ApiResponse.<String>builder()
                .result("Hello")
                .build();
    }
}
