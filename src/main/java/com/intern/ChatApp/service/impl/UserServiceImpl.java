package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.dto.request.AssignRoleRequest;
import com.intern.ChatApp.dto.request.UserRequest;
import com.intern.ChatApp.dto.response.RoleResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.Role;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.RoleRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.EmailService;
import com.intern.ChatApp.service.FileService;
import com.intern.ChatApp.service.UserService;
import com.intern.ChatApp.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmailService emailService;

    public UserResponse addUser(UserRequest userRequest) {

        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setName(userRequest.getName());
        user.setRole(role);

        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .imagePath(user.getImagePath())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDisabled(user.getIsDisabled())
                .roleResponse(new RoleResponse(
                        user.getRole().getId()
                        ,user.getRole().getRoleName()))
                .build();
    }

    @Override
    public Page<UserResponse> getAllUsers(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort[0]).ascending());
        return userRepository.findAllByIsDisabledFalse(pageable).map(this::convertToUserResponse);
    }


    @Override
    @Transactional
    public UserResponse updateProfile(String name, String oldPassword, String newPassword, MultipartFile image) {
        User currentUser = getCurrentUser();

        if (name != null && !name.isBlank()) {
            currentUser.setName(name);
        }

        if (oldPassword != null && newPassword != null) {
            if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
            }
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }

        if (image != null && !image.isEmpty()) {
            String filename = fileService.saveFile(image);
            currentUser.setImagePath(filename);
        }

        currentUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(currentUser);

        return convertToUserResponse(currentUser);
    }


    private User getCurrentUser() {
        String email = securityUtil.extractEmailFromSecurityContext();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserResponse getCurrent() {
        String currentUserEmail = securityUtil.extractEmailFromSecurityContext();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoleResponse roleResponse = new RoleResponse(
                user.getRole().getId(),
                user.getRole().getRoleName());

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getImagePath(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getIsDisabled(),
                roleResponse
        );

    }




    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .imagePath(user.getImagePath())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDisabled(user.getIsDisabled())
                .roleResponse(new RoleResponse(
                        user.getRole().getId()
                        ,user.getRole().getRoleName()))
                .build();
    }


    @Override
    public User getUser(int id) {
        return null;
    }

    public void disableUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setIsDisabled(!user.getIsDisabled());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
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

    @Transactional
    @Override
    public void resetPassword(String email) {
        // Tìm người dùng theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tạo mật khẩu mới ngẫu nhiên
        String newPassword = generateRandomPassword(8);

        // Mã hóa mật khẩu và lưu vào DB
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // Gửi mật khẩu mới qua email
        String subject = "Yêu cầu reset mật khẩu";
        String content = "Mật khẩu mới của bạn là: " + newPassword;
        emailService.sendEmail(user.getEmail(), subject, content);
    }

    // Hàm tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
