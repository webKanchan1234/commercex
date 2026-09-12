package com.commercex.auth.dto.response;

import com.commercex.auth.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Authenticated User")
public record UserResponse(

        UUID id,

        String firstName,

        String lastName,

        String email,

        String phone,

        UserStatus status,

        Set<String> roles

) {
}