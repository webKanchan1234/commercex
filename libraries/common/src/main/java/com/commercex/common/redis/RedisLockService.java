package com.commercex.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration LOCK_TTL = Duration.ofSeconds(5);

    public boolean acquireLock(String key) {

        Boolean success = redisTemplate
                .opsForValue()
                .setIfAbsent(key, "LOCKED", LOCK_TTL);

        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }

}
