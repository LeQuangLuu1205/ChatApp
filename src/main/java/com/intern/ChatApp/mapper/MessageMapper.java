package com.intern.ChatApp.mapper;

import com.intern.ChatApp.dto.request.MessageRequest;
import com.intern.ChatApp.dto.response.MessageResponse;
import com.intern.ChatApp.dto.response.RoleResponse;
import com.intern.ChatApp.dto.response.UserResponse;
import com.intern.ChatApp.entity.Message;
import com.intern.ChatApp.entity.Role;
import com.intern.ChatApp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "userResponse", source = "user", qualifiedByName = "mapToUserResponse")
    MessageResponse toMessageResponse(Message message);

    @Named("mapToUserResponse")
    default UserResponse mapToUserResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .imagePath(user.getImagePath())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDisabled(user.getIsDisabled())
                .roleResponse(mapToRoleResponse(user.getRole()))
                .build();
    }

    default RoleResponse mapToRoleResponse(Role role) {
        if (role == null) return null;

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getRoleName())
                .build();
    }
}