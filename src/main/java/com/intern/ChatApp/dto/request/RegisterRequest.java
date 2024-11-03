package com.intern.ChatApp.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @Email(message = "INVALID_REQUEST")
    private String email;
    private String password;
    private String name;
}
