package com.commercex.auth.scheduler;

import com.commercex.auth.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SessionCleanupScheduler {

    private final UserSessionRepository repository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanup(){

        repository.deleteByExpiresAtBefore(
                LocalDateTime.now()
        );

    }

}
