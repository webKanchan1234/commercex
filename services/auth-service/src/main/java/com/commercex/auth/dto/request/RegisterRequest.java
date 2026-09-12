package com.commercex.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "User Registration Request")
public record RegisterRequest(

        @Schema(
                description = "First Name",
                example = "Kanchan"
        )
        @NotBlank(message = "First name is required")
        @Size(min = 2,max = 50)
        String firstName,

        @Schema(
                description = "Last Name",
                example = "Kumar"
        )
        @NotBlank(message = "Last name is required")
        @Size(min = 2,max = 50)
        String lastName,

        @Schema(
                description = "Email Address",
                example = "kanchan@test.com"
        )
        @Email(message = "Invalid email")
        @NotBlank
        String email,

        @Schema(
                description = "Phone Number",
                example = "9876543210"
        )
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid phone number"
        )
        String phone,

        @Schema(
                description = "Password",
                example = "Password@123"
        )
        @Size(min = 8,max = 20)
        String password,

        @Schema(
                description = "Confirm Password",
                example = "Password@123"
        )
        @Size(min = 8,max = 20)
        String confirmPassword

) {
}