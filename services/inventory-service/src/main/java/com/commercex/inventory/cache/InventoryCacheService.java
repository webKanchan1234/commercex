package com.commercex.inventory.cache;

import com.commercex.common.constants.RedisConstants;
import com.commercex.inventory.dto.response.InventoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryCacheService {

    private static final Duration CACHE_TTL =
            Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String key(UUID productId) {
        return RedisConstants.INVENTORY_KEY + productId;
    }

    public InventoryResponse get(UUID productId) {

        Object value =
                redisTemplate.opsForValue().get(key(productId));

        if (value == null) {
            return null;
        }

        return objectMapper.convertValue(
                value,
                InventoryResponse.class
        );
    }

    public void save(InventoryResponse response) {

        redisTemplate.opsForValue().set(
                key(response.getProductId()),
                response,
                CACHE_TTL
        );
    }

    public void delete(UUID productId) {

        redisTemplate.delete(key(productId));
    }

}