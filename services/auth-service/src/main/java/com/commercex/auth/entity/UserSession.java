package com.commercex.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_sessions",
        indexes = {

                @Index(
                        name = "idx_session_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_session_refresh_token",
                        columnList = "refreshToken",
                        unique = true
                ),

                @Index(
                        name = "idx_session_revoked",
                        columnList = "revoked"
                )

        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Owner of this session
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    /**
     * Current Refresh Token
     */
    @Column(
            nullable = false,
            unique = true,
            length = 1000
    )
    private String refreshToken;

    /**
     * Chrome / Safari / Firefox
     */
    @Column(length = 50)
    private String browser;

    /**
     * Windows / macOS / Android
     */
    @Column(length = 50)
    private String operatingSystem;

    /**
     * Mobile / Desktop / Tablet
     */
    @Column(length = 30)
    private String deviceType;

    /**
     * MacBook Pro
     * Pixel 9
     */
    @Column(length = 150)
    private String deviceName;

    /**
     * Complete browser User-Agent
     */
    @Column(length = 2000)
    private String userAgent;

    /**
     * Client IP
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * Last activity
     */
    @Column(nullable = false)
    private LocalDateTime lastUsedAt;

    /**
     * Session expiry
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Logout / Revoked
     */
    @Column(nullable = false)
    private boolean revoked;

    /**
     * Login Time
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Updated every refresh
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private boolean trustedDevice;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
        lastUsedAt = now;

        revoked = false;
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}