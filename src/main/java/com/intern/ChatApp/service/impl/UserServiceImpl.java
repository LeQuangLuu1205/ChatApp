package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.dto.request.AssignRoleRequest;
import com.intern.ChatApp.entity.Role;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.RoleRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void addUser(User user) {

    }

    @Override
    public List<User> getUser() {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public void updateUser(int id, User user) {

    }

    @Override
    public void assignRole(Integer userId,AssignRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Role newRole = roleRepository.findByRoleName(request.getRole())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRole(newRole);
        userRepository.save(user);
    }
}
