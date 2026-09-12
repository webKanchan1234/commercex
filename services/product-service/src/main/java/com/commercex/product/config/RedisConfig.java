//package com.commercex.product.config;
//
//import com.commercex.product.dto.response.ProductResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableCaching
//@RequiredArgsConstructor
//public class RedisConfig {
//
//    private final ObjectMapper objectMapper;
//
////    @Bean
////    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
////
////        GenericJackson2JsonRedisSerializer serializer =
////                new GenericJackson2JsonRedisSerializer(objectMapper);
////
////        RedisCacheConfiguration defaultConfig =
////                RedisCacheConfiguration.defaultCacheConfig()
////                        .entryTtl(Duration.ofMinutes(10))
////                        .disableCachingNullValues()
////                        .serializeValuesWith(
////                                RedisSerializationContext.SerializationPair
////                                        .fromSerializer(serializer)
////                        );
////
////        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
////        configs.put("products", defaultConfig.entryTtl(Duration.ofMinutes(10)));
////        configs.put("brands", defaultConfig.entryTtl(Duration.ofHours(1)));
////        configs.put("categories", defaultConfig.entryTtl(Duration.ofHours(1)));
////        configs.put("productSearch", defaultConfig.entryTtl(Duration.ofMinutes(5)));
////
////        return RedisCacheManager.builder(connectionFactory)
////                .cacheDefaults(defaultConfig)
////                .withInitialCacheConfigurations(configs)
////                .transactionAware()
////                .build();
////    }
//
//
////    @Bean
////    public RedisTemplate<String, ProductResponse> productRedisTemplate(
////            RedisConnectionFactory connectionFactory,
////            ObjectMapper objectMapper) {
////
////        RedisTemplate<String, ProductResponse> template = new RedisTemplate<>();
////
////        template.setConnectionFactory(connectionFactory);
////
////        Jackson2JsonRedisSerializer<ProductResponse> serializer =
////                new Jackson2JsonRedisSerializer<>(objectMapper, ProductResponse.class);
////
////        template.setKeySerializer(new StringRedisSerializer());
////        template.setValueSerializer(serializer);
////        template.setHashKeySerializer(new StringRedisSerializer());
////        template.setHashValueSerializer(serializer);
////
////        template.afterPropertiesSet();
////
////        return template;
////    }
//}