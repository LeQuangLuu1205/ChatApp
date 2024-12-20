package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.request.AssignRoleRequest;
import com.intern.ChatApp.dto.request.SendPasswordRequest;
import com.intern.ChatApp.dto.request.UserRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping()
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("User has been added successfully.")
                        .result(userResponse)
                        .build()
        );
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getUserById(@RequestParam int id) {
        User user=userService.getUser(id);

        return ApiResponse.builder().result(user).build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@PathVariable int id , @RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        userService.updateUser(id, user);

        return "Success update user";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.disableUser(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(1000)
                        .message("User disabled successfully")
                        .build()
        );
    }
    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> assignRole(@PathVariable Integer userId, @RequestBody AssignRoleRequest request) {
        userService.assignRole(userId, request);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Role assigned successfully")
                        .message("User role updated")
                        .build()
        );
    }
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "oldPassword", required = false) String oldPassword,
            @RequestPart(value = "newPassword", required = false) String newPassword,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        UserResponse updatedUser = userService.updateProfile(name, oldPassword, newPassword, image);

        return ResponseEntity.ok(new ApiResponse<>(1000, "Profile updated successfully", updatedUser));
    }

    @PostMapping("/send-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody SendPasswordRequest request) {
        userService.resetPassword(request.getEmail());
        return ResponseEntity.ok(new ApiResponse<>(1000, "Mật khẩu mới đã được gửi đến email của bạn.",null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse userResponse = userService.getCurrent();
        return ResponseEntity.ok(new ApiResponse<>(1000,"Thông tin tài khoản hiện tại",userResponse));
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    public ApiResponse<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Page<UserResponse> list = userService.getAllUsers(page, size, sort);

        return new ApiResponse<>(1000, "Lấy danh sách thành công", list);
    }
}
