package com.commercex.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "session")
public class SessionProperties {

    /**
     * Maximum simultaneous active sessions.
     */
    private int maxActiveSessions = 15;

    /**
     * Idle timeout before session expires.
     */
    private int idleTimeoutMinutes = 30;

}