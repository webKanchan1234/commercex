package com.commercex.auth.mapper;

import com.commercex.auth.dto.request.RegisterRequest;
import com.commercex.auth.dto.response.UserResponse;
import com.commercex.auth.entity.Role;
import com.commercex.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "failedAttempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
    default String map(Role role) {
        return role.getName();
    }
}