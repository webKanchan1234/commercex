package com.commercex.auth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionResponse(

        UUID sessionId,

        String browser,

        String operatingSystem,

        String deviceType,

        String deviceName,

        String ipAddress,

        LocalDateTime lastUsedAt,

        LocalDateTime expiresAt,

        boolean currentSession
) {}
