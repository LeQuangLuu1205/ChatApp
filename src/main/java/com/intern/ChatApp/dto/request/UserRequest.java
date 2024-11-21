package com.intern.ChatApp.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserRequest {

    @Email(message = "Invalid email format")
    private String email;

    private String password;

    private String name;

    private Integer roleId;
}
