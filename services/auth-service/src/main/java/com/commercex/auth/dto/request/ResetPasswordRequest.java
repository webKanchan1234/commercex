package com.commercex.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

        @NotBlank
        String token,

        @Size(min = 8,max = 20)
        String password,

        @Size(min = 8,max = 20)
        String confirmPassword

) {
}