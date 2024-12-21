package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.AssignRoleRequest;
import com.intern.ChatApp.dto.request.UserRequest;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponse addUser(UserRequest userRequest);

    User getUser(int id);

    UserResponse updateProfile(String name, String oldPassword, String newPassword, MultipartFile image);

    void disableUser(Integer userId);

    void updateUser(int id,User user);

    void assignRole(Integer userId,AssignRoleRequest request);
    void resetPassword(String email);
    UserResponse getCurrent();

    Page<UserResponse> getAllUsers(int page, int size, String[] sort);
}
