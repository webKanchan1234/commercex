package com.commercex.auth.controller;

import com.commercex.auth.dto.request.*;
import com.commercex.auth.dto.response.LoginResponse;
import com.commercex.auth.dto.response.RegisterResponse;
import com.commercex.auth.dto.response.SessionResponse;
//import com.commercex.common.response.ApiResponse;
import com.commercex.auth.service.AuthService;
import com.commercex.common.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication APIs including registration, login, refresh token, password reset, email verification and session management."
)
public class AuthController {

    private final AuthService authService;


    @Operation(
            summary = "Register User",
            description = "Creates a new CommerceX user account."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ApiSuccessResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ApiSuccessResponse.success(
                authService.register(request)
        );
    }

//    @PostMapping("/login")
//    public ApiResponse<LoginResponse> login(
//            @Valid @RequestBody LoginRequest request) {
//
//        return ApiResponse.success(
//                authService.login(request)
//        );
//    }

    @PostMapping("/login")
    public ApiSuccessResponse<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request,
            HttpServletRequest servletRequest
    ) {
        return ApiSuccessResponse.success(

                authService.login(
                        request,
                        servletRequest
                )

        );

    }


    @Operation(
            summary = "Refresh Access Token",
            description = "Generates new Access Token using Refresh Token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalid")
    })
    @PostMapping("/refresh")
    public ApiSuccessResponse<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        System.out.println("Refresh API Called");

        return ApiSuccessResponse.success(
                authService.refreshToken(request)
        );
    }


    @Operation(
            summary = "Verify Email",
            description = "Verifies email using verification token."
    )
    @GetMapping("/verify")
    public ApiSuccessResponse<Void> verifyEmail(
            @RequestParam("token") String token) {

        authService.verifyEmail(token);

        return ApiSuccessResponse.success("Email verified successfully");
    }


    @Operation(
            summary = "Forgot Password",
            description = "Sends password reset email."
    )
    @PostMapping("/forgot-password")
    public ApiSuccessResponse<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ApiSuccessResponse.success("Email verified successfully");
    }


    @Operation(
            summary = "Reset Password",
            description = "Changes user password using reset token."
    )
    @PostMapping("/reset-password")
    public ApiSuccessResponse<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        System.out.println("Reset Password Called");

        authService.resetPassword(request);

        return ApiSuccessResponse.success("Password changed successfully.");
    }


    @Operation(
            summary = "Current User Sessions",
            description = "Returns all active sessions of authenticated user."
    )
    @GetMapping("/sessions")
    public ApiSuccessResponse<List<SessionResponse>> sessions(
            Authentication authentication) {

        return ApiSuccessResponse.success(

                authService.getSessions(
                        authentication.getName()
                )

        );
    }


    @Operation(
            summary = "Logout Current Device",
            description = "Revokes current device session."
    )
    @PostMapping("/logout")
    public ApiSuccessResponse<Void> logout(
            @RequestBody RefreshTokenRequest request) {

        authService.logoutCurrentDevice(
                request.refreshToken()
        );

        return ApiSuccessResponse.success(
                "Logged out successfully"
        );
    }


    @Operation(
            summary = "Logout All Devices",
            description = "Revokes all sessions of current user."
    )
    @PostMapping("/logout-all")
    public ApiSuccessResponse<Void> logoutAll(
            Authentication authentication) {

        authService.logoutAllDevices(
                authentication.getName()
        );

        return ApiSuccessResponse.success(
                "Logged out from all devices"
        );
    }

}