package com.intern.ChatApp.controller;

import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('MODERATOR')")
    public ApiResponse<?> getAllUsers() {
        return ApiResponse.<String>builder()
                .result("Hello")
                .build();
    }

    @GetMapping("/getdemo")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getDemoUsers() {
        return ApiResponse.<String>builder()
            .result("Hello")
            .build();
    }

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Thiết lập mật khẩu đã mã hóa

        userService.addUser(user);

        return "Success add user";
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getAllUser() {
        List<User> users=userService.getUser();

        return ApiResponse.builder().result(users).build();
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
        // Mã hóa mật khẩu nếu có mật khẩu mới được cung cấp
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword); // Thiết lập mật khẩu đã mã hóa
        }

        userService.updateUser(id, user);

        return "Success update user";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);

        return "Success delete user";
    }
}
