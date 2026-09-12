package com.commercex.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT Token Response")
public record TokenResponse(

        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String refreshToken,

        @Schema(example = "Bearer")
        String tokenType,

        @Schema(example = "900000")
        Long expiresIn

) {
}