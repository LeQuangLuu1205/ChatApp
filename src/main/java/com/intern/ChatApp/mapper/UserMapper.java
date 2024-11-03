package com.intern.ChatApp.mapper;

import com.intern.ChatApp.dto.request.RegisterRequest;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)  // Để xử lý riêng trong service
    User toUser(RegisterRequest registerRequestDTO);
    UserResponse toUserResponseDTO(User user);
}
