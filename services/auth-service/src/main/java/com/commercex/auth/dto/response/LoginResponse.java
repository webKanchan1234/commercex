package com.commercex.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login Response")
public record LoginResponse(

        @Schema(description = "Logged-in User")
        UserResponse user,

        @Schema(description = "JWT Tokens")
        TokenResponse token

) {
}