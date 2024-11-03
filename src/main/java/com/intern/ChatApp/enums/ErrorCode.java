package com.intern.ChatApp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(1001, "Invalid request", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.CONFLICT),
    INVALID_KEY(1003, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(1005, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    ROLE_NOT_FOUND(1006,"Role not found",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    // Mã cho xác minh email
    VERIFY_SUCCESS(1020, "Email verified successfully", HttpStatus.OK),
    INVALID_VERIFICATION_LINK(1021, "Invalid verification link", HttpStatus.BAD_REQUEST);
    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}