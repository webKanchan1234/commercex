package com.commercex.auth.service;

import com.commercex.auth.config.SessionProperties;
import com.commercex.auth.device.DeviceInfo;
import com.commercex.auth.device.DeviceInfoService;
import com.commercex.auth.dto.request.*;
import com.commercex.auth.dto.response.*;
import com.commercex.auth.entity.*;
import com.commercex.auth.enums.RoleName;
import com.commercex.auth.mapper.UserMapper;
import com.commercex.auth.notification.EmailService;
import com.commercex.auth.repository.*;
import com.commercex.auth.security.jwt.JwtProperties;
import com.commercex.auth.security.jwt.JwtProvider;
import com.commercex.auth.security.model.CustomUserPrincipal;
import com.commercex.common.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;

    private final DeviceInfoService deviceInfoService;

    private final JwtProperties jwtProperties;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final SessionProperties sessionProperties;

//    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final UserSessionRepository userSessionRepository;


    @Override
    public RegisterResponse register(RegisterRequest request) {

        validateRegistration(request);

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        Role role = roleRepository
                .findByName(RoleName.ROLE_CUSTOMER.name())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.RESOURCE_NOT_FOUND,
                                "Default role not found"
                        )
                );
        user.getRoles().add(role);
        String token = UUID.randomUUID().toString();
        user.setEnabled(false);
        User savedUser = userRepository.save(user);

        EmailVerificationToken verificationToken =
                EmailVerificationToken.builder()

                        .token(token)

                        .user(savedUser)

                        .expiryDate(
                                LocalDateTime.now().plusHours(24)
                        )

                        .build();

        emailVerificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(
                savedUser,
                token
        );

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                "Registration successful. Please verify your email."
        );

    }


    @Override
    @Transactional
    public LoginResponse refreshToken(
            RefreshTokenRequest request) {

    /*
     ==========================================================
                    STEP 1
            Validate JWT Signature
     ==========================================================
     */

        if (!jwtProvider.validateToken(request.refreshToken())) {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED,
                    "Invalid refresh token"
            );
        }

    /*
     ==========================================================
                    STEP 2
        Ensure this is a Refresh Token
     ==========================================================
     */

        if (!jwtProvider.isRefreshToken(request.refreshToken())) {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED,
                    "Not a refresh token"
            );
        }

    /*
     ==========================================================
                    STEP 3
        Find Current User Session
     ==========================================================
     */

        Optional<UserSession> optionalSession =
                userSessionRepository
                        .findByRefreshToken(
                                request.refreshToken()
                        );

        if(optionalSession.isEmpty()){

            String email =
                    jwtProvider.extractUsername(
                            request.refreshToken()
                    );

            userRepository.findByEmail(email)
                    .ifPresent(user -> {

                        List<UserSession> sessions =
                                userSessionRepository
                                        .findByUserAndRevokedFalse(user);

                        sessions.forEach(
                                session ->
                                        session.setRevoked(true)
                        );

                        userSessionRepository.saveAll(sessions);

                    });

            throw new UnauthorizedException(

                    ErrorCode.UNAUTHORIZED,

                    "Refresh token reuse detected. All sessions revoked."

            );
        }

        UserSession session =
                optionalSession.get();



    /*
     ==========================================================
                    STEP 4
            Session Revoked?
     ==========================================================
     */

        if (session.isRevoked()) {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED,
                    "Session revoked"
            );
        }

    /*
     ==========================================================
                    STEP 5
            Session Expired?
     ==========================================================
     */

        if (session.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            userSessionRepository.delete(session);

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED,
                    "Session expired"
            );
        }

    /*
     ==========================================================
                    STEP 6
            Generate New Tokens
     ==========================================================
     */

        User user = session.getUser();

        CustomUserPrincipal principal =
                new CustomUserPrincipal(user);

        String accessToken =
                jwtProvider.generateAccessToken(principal);

        String refreshToken =
                jwtProvider.generateRefreshToken(principal);

    /*
     ==========================================================
              OLD IMPLEMENTATION

        delete old row

        insert new row

     ==========================================================

        refreshTokenRepository.delete(...)

        refreshTokenRepository.save(...)

     ==========================================================
              NEW IMPLEMENTATION

        Update Existing Session

     ==========================================================
     */

        session.setRefreshToken(refreshToken);

        session.setLastUsedAt(
                LocalDateTime.now()
        );

        session.setExpiresAt(
                LocalDateTime.now().plusDays(7)
        );

        session.setRevoked(false);

        userSessionRepository.save(session);

    /*
     ==========================================================
                Build Response
     ==========================================================
     */

        return new LoginResponse(

                userMapper.toResponse(user),

                new TokenResponse(

                        accessToken,

                        refreshToken,

                        "Bearer",

                        jwtProperties
                                .getAccessTokenExpiration()

                )
        );

    }

    //old version
//    @Override
//    public LoginResponse refreshToken(
//            RefreshTokenRequest request) {
//
//        // Validate signature
//        if (!jwtProvider.validateToken(request.refreshToken())) {
//
//            throw new UnauthorizedException(
//                    ErrorCode.UNAUTHORIZED,
//                    "Invalid refresh token"
//            );
//        }
//
//        // Ensure it is a refresh token
//        if (!jwtProvider.isRefreshToken(request.refreshToken())) {
//
//            throw new UnauthorizedException(
//                    ErrorCode.UNAUTHORIZED,
//                    "Not a refresh token"
//            );
//        }
//
//        // Find token in database
//        RefreshToken storedToken =
//                refreshTokenRepository
//                        .findByToken(request.refreshToken())
//                        .orElseThrow(() ->
//                                new UnauthorizedException(
//                                        ErrorCode.UNAUTHORIZED,
//                                        "Refresh token not found"
//                                ));
//
//        // Check revoked
//        if (storedToken.isRevoked()) {
//
//            throw new UnauthorizedException(
//                    ErrorCode.UNAUTHORIZED,
//                    "Refresh token revoked"
//            );
//        }
//
//        // Check expiry
//        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
//
//            refreshTokenRepository.delete(storedToken);
//
//            throw new UnauthorizedException(
//                    ErrorCode.UNAUTHORIZED,
//                    "Refresh token expired"
//            );
//        }
//
//        User user = storedToken.getUser();
//
//        CustomUserPrincipal principal =
//                new CustomUserPrincipal(user);
//
//        String accessToken =
//                jwtProvider.generateAccessToken(principal);
//
//        String newRefreshToken =
//                jwtProvider.generateRefreshToken(principal);
//
//        // Rotate refresh token
//        refreshTokenRepository.delete(storedToken);
//
//        RefreshToken entity =
//                RefreshToken.builder()
//                        .token(newRefreshToken)
//                        .user(user)
//                        .expiryDate(LocalDateTime.now().plusDays(7))
//                        .revoked(false)
//                        .build();
//
//        System.out.println("Saving refresh token...");
//
//        refreshTokenRepository.save(entity);
//
//        System.out.println("Refresh token saved successfully.");
//
//        return new LoginResponse(
//
//                userMapper.toResponse(user),
//
//                new TokenResponse(
//                        accessToken,
//                        newRefreshToken,
//                        "Bearer",
//                        jwtProperties.getAccessTokenExpiration()
//                )
//        );
//    }





    private void validateRegistration(RegisterRequest request) {

        if (!request.password().equals(request.confirmPassword())) {
            throw new BadRequestException(
                    "Passwords do not match"
            );
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException(ErrorCode.CONFLICT,
                    "Email already exists"
            );
        }

        if (userRepository.existsByPhone(request.phone())) {
            throw new ConflictException(ErrorCode.CONFLICT,
                    "Phone already exists"
            );
        }

    }



    @Override
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest servletRequest) {

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        DeviceInfo device =
                deviceInfoService.extractDeviceInfo(
                        servletRequest
                );

        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        if (!principal.getUser().isEnabled()) {

            throw new UnauthorizedException(
                    ErrorCode.UNAUTHORIZED,
                    "Please verify your email first."
            );
        }

        User user = principal.getUser();

    /*
    ==========================================================
                    ACCESS TOKEN
    ==========================================================
    */

        String accessToken =
                jwtProvider.generateAccessToken(principal);

    /*
    ==========================================================
                    REFRESH TOKEN
    ==========================================================
    */

        String refreshToken =
                jwtProvider.generateRefreshToken(principal);

    /*
    ==========================================================
                    VERSION 1 (OLD)
    ==========================================================

    Earlier we only stored the Refresh Token.

    RefreshToken entity =
            RefreshToken.builder()
                    .token(refreshToken)
                    .user(user)
                    .expiryDate(
                            LocalDateTime.now().plusDays(7)
                    )
                    .revoked(false)
                    .build();

    refreshTokenRepository.save(entity);

    Problems

    ❌ No Browser
    ❌ No Device
    ❌ No OS
    ❌ No IP
    ❌ No User-Agent
    ❌ No Multi-device Login
    ❌ Cannot Logout Individual Device
    ❌ Cannot Show Active Sessions

    ==========================================================
                    VERSION 2 (CURRENT)
    ==========================================================

    Instead of storing only a Refresh Token,
    we create a complete User Session.

    One Login = One Session

    Future Features

    ✅ Multiple Devices
    ✅ Session History
    ✅ Logout Current Device
    ✅ Logout All Devices
    ✅ Refresh Token Rotation
    ✅ Device Tracking

    ==========================================================
    */

        long activeSessions =

                userSessionRepository
                        .countByUserAndRevokedFalse(user);

        if (activeSessions >=
                sessionProperties.getMaxActiveSessions()) {

            throw new UnauthorizedException(

                    ErrorCode.UNAUTHORIZED,

                    "Maximum active sessions reached. Please logout from another device."

            );
        }

        UserSession session =
                UserSession.builder()

                        .user(user)

                        .refreshToken(refreshToken)

                        .browser(device.browser())

                        .operatingSystem(
                                device.operatingSystem()
                        )

                        .deviceType(
                                device.deviceType()
                        )

                        .deviceName(
                                device.deviceName()
                        )

                        .userAgent(
                                device.userAgent()
                        )

                        .ipAddress(
                                device.ipAddress()
                        )

                        .lastUsedAt(
                                LocalDateTime.now()
                        )

                        .expiresAt(
                                LocalDateTime.now()
                                        .plusDays(7)
                        )

                        .revoked(false)

                        .build();

        userSessionRepository.save(session);

    /*
    ==========================================================
                UPDATE USER LOGIN TIME
    ==========================================================
    */

        if (session.getLastUsedAt()

                .plusMinutes(
                        sessionProperties.getIdleTimeoutMinutes()
                )

                .isBefore(LocalDateTime.now())) {

            session.setRevoked(true);

            userSessionRepository.save(session);

            throw new UnauthorizedException(

                    ErrorCode.UNAUTHORIZED,

                    "Session expired due to inactivity"

            );
        }

        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        TokenResponse tokenResponse =
                new TokenResponse(
                        accessToken,
                        refreshToken,
                        "Bearer",
                        jwtProperties.getAccessTokenExpiration()
                );

        UserResponse userResponse =
                userMapper.toResponse(user);

        return new LoginResponse(
                userResponse,
                tokenResponse
        );
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {

        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository

                        .findByToken(token)

                        .orElseThrow(() ->

                                new BadRequestException(
                                        "Invalid verification token"
                                ));

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "Verification token expired"
            );

        }

        User user = verificationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        emailVerificationTokenRepository
                .delete(verificationToken);

    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request){

        Optional<User> optionalUser =
                userRepository.findByEmail(request.email());

        if(optionalUser.isEmpty()){

            return;
        }

        User user = optionalUser.get();

        passwordResetTokenRepository
                .findByUser(user)
                .ifPresent(passwordResetTokenRepository::delete);

        String token =
                UUID.randomUUID().toString();

        PasswordResetToken resetToken =
                PasswordResetToken.builder()

                        .token(token)

                        .user(user)

                        .expiryDate(
                                LocalDateTime.now().plusMinutes(30)
                        )

                        .build();

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
                user,
                token
        );

    }


    @Override
    @Transactional
    public void resetPassword(
            ResetPasswordRequest request){

        if(!request.password()
                .equals(request.confirmPassword())){

            throw new BadRequestException(
                    "Passwords do not match"
            );
        }

        PasswordResetToken token =
                passwordResetTokenRepository

                        .findByToken(request.token())

                        .orElseThrow(() ->

                                new BadRequestException(
                                        "Invalid reset token"
                                ));

        if(token.getExpiryDate()
                .isBefore(LocalDateTime.now())){

            passwordResetTokenRepository.delete(token);

            throw new BadRequestException(
                    "Reset token expired"
            );

        }

        User user =
                token.getUser();

        user.setPassword(

                passwordEncoder.encode(
                        request.password()
                )

        );
        user.setTokenVersion(
                user.getTokenVersion() + 1
        );

        userRepository.save(user);

        List<UserSession> sessions =
                userSessionRepository
                        .findByUserAndRevokedFalse(user);

        sessions.forEach(

                session ->

                        session.setRevoked(true)

        );

        userSessionRepository.saveAll(sessions);

        passwordResetTokenRepository.delete(token);

    }



    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getSessions(
            String email
    ) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "User not found"
                                ));

        return userSessionRepository
                .findByUserAndRevokedFalse(user)
                .stream()
                .map(session ->

                        new SessionResponse(

                                session.getId(),

                                session.getBrowser(),

                                session.getOperatingSystem(),

                                session.getDeviceType(),

                                session.getDeviceName(),

                                session.getIpAddress(),

                                session.getLastUsedAt(),

                                session.getExpiresAt(),

                                false
                        )

                )
                .toList();
    }

    @Override
    @Transactional
    public void logoutCurrentDevice(
            String refreshToken) {

        UserSession session =
                userSessionRepository
                        .findByRefreshToken(refreshToken)
                        .orElseThrow(() ->

                                new UnauthorizedException(
                                        ErrorCode.UNAUTHORIZED,
                                        "Session not found"
                                ));

        session.setRevoked(true);

        userSessionRepository.save(session);
    }

    @Override
    @Transactional
    public void logoutAllDevices(
            String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        ErrorCode.RESOURCE_NOT_FOUND,
                                        "User not found"
                                ));

        List<UserSession> sessions =
                userSessionRepository
                        .findByUserAndRevokedFalse(user);

        sessions.forEach(session ->
                session.setRevoked(true));

        userSessionRepository.saveAll(sessions);
    }
}
