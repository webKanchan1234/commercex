package com.commercex.auth.service;

import com.commercex.auth.dto.request.*;
import com.commercex.auth.dto.response.LoginResponse;
import com.commercex.auth.dto.response.RegisterResponse;
import com.commercex.auth.dto.response.SessionResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

//    LoginResponse login(LoginRequest request);  // refresh token
    LoginResponse login(LoginRequest request, HttpServletRequest servletRequest); //refresh token rotation

    LoginResponse refreshToken(RefreshTokenRequest request);
    public void verifyEmail(String token);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(
            ResetPasswordRequest request
    );

    List<SessionResponse> getSessions(String email);

    void logoutCurrentDevice(String refreshToken);

    void logoutAllDevices(String email);
}