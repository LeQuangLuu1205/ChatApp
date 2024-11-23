package com.intern.ChatApp.service;

import com.intern.ChatApp.dto.request.AssignRoleRequest;
import com.intern.ChatApp.dto.request.UserRequest;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.User;

import java.util.List;

public interface UserService {
    UserResponse addUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    User getUser(int id);

    void deleteUser(int id);

    void updateUser(int id,User user);

    void assignRole(Integer userId,AssignRoleRequest request);
}
