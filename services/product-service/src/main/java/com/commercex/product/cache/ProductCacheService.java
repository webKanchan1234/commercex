package com.commercex.product.cache;

import com.commercex.common.redis.RedisConstants;
import com.commercex.product.dto.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private static final Duration PRODUCT_TTL = Duration.ofMinutes(10);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private String key(UUID id) {
        return RedisConstants.PRODUCT_KEY + id;
    }

    public ProductResponse get(UUID id) {

        Object value = redisTemplate.opsForValue().get(key(id));

        if (value == null) {
            return null;
        }

        return objectMapper.convertValue(value, ProductResponse.class);
    }

    public void save(ProductResponse response) {
        redisTemplate.opsForValue().set(
                key(response.getId()),
                response,
                PRODUCT_TTL
        );
    }

    public void delete(UUID id) {
        redisTemplate.delete(key(id));
    }
}