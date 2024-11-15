package com.intern.ChatApp.service.impl;

import com.intern.ChatApp.config.CustomUserDetailService;
import com.intern.ChatApp.dto.request.IntrospectRequest;
import com.intern.ChatApp.dto.request.LoginRequest;
import com.intern.ChatApp.dto.request.LogoutRequest;
import com.intern.ChatApp.dto.request.RegisterRequest;
import com.intern.ChatApp.dto.response.ApiResponse;
import com.intern.ChatApp.dto.response.AuthenticationResponse;
import com.intern.ChatApp.dto.response.IntrospectResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.InvalidatedToken;
import com.intern.ChatApp.entity.Role;
import com.intern.ChatApp.entity.User;
import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.mapper.UserMapper;
import com.intern.ChatApp.repository.InvalidatedTokenRepository;
import com.intern.ChatApp.repository.RoleRepository;
import com.intern.ChatApp.repository.UserRepository;
import com.intern.ChatApp.service.AuthService;
import com.intern.ChatApp.service.EmailService;
import com.intern.ChatApp.utils.JwtUtilsHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilsHelper jwtUtils;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        // Ánh xạ từ RegisterRequest sang User
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRole(role);

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Tạo URL xác minh
        String verificationUrl = "http://localhost:8090/api/auth/verify?email=" + savedUser.getEmail();
        // Gửi email xác minh
        emailService.sendVerificationEmail(user.getEmail(), verificationUrl);

        return userMapper.toUserResponseDTO(savedUser);
    }

    public ApiResponse<String> verifyUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_VERIFICATION_LINK));

        if (user.getIsVerified()) {
            return ApiResponse.<String>builder()
                    .code(ErrorCode.VERIFY_SUCCESS.getCode())
                    .message("Email is already verified")
                    .build();
        }

        user.setIsVerified(true);
        userRepository.save(user);

        return ApiResponse.<String>builder()
                .code(ErrorCode.VERIFY_SUCCESS.getCode())
                .message(ErrorCode.VERIFY_SUCCESS.getMessage())
                .build();
    }

    @Override
    public AuthenticationResponse authenticateUser(LoginRequest request) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            var token = jwtUtils.generateToken(userDetails);
            return AuthenticationResponse.builder().token(token).authenticated(true).build();
        } catch (BadCredentialsException e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout(LogoutRequest request) {
        try {
            Claims claims = verifyToken(request.getToken(), true);

            String jwtId = claims.getId();
            Date expiryTime = claims.getExpiration();

            invalidatedTokenRepository.deleteById(jwtId);

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException exception) {
            System.out.println("Token is invalid or already expired");
        }
    }



    public Claims verifyToken(String token, boolean isRefresh) {
        Claims claims = jwtUtils.extractAllClaims(token);

        Date expiryTime = claims.getExpiration();

        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String jwtId = claims.getId();
        if (invalidatedTokenRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return claims;
    }
}
