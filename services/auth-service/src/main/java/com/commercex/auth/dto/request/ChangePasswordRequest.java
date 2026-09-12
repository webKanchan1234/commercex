package com.commercex.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank
        String oldPassword,

        @Size(min = 8,max = 20)
        String newPassword,

        @Size(min = 8,max = 20)
        String confirmPassword

) {
}