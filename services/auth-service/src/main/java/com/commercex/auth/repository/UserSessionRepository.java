package com.commercex.auth.repository;

import com.commercex.auth.entity.User;
import com.commercex.auth.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository
        extends JpaRepository<UserSession, UUID> {

    Optional<UserSession>
    findByRefreshToken(String refreshToken);

    List<UserSession>
    findByUser(User user);

    List<UserSession>
    findByUserAndRevokedFalse(User user);

    void deleteByUser(User user);
    long countByUserAndRevokedFalse(User user);
    void deleteByRefreshToken(String refreshToken);
    void deleteByExpiresAtBefore(
            LocalDateTime now
    );
}