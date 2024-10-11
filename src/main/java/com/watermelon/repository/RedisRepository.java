package com.watermelon.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@RequiredArgsConstructor
@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisRepository<T,K> {

    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper redisObjectMapper;
    
    @NonFinal
    HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }
    @NonFinal
    @Value("${redis.cart.expire-day-time}")
    int expireDayTime;

    public K save(String redisHashKey, K id, T object) throws JsonProcessingException {
        String json = redisObjectMapper.writeValueAsString(object);
        hashOperations.put(redisHashKey, String.valueOf(id), json);

        Instant expireTime = Instant.now().plus(expireDayTime, ChronoUnit.DAYS);
        redisTemplate.expireAt(redisHashKey, expireTime);
        return id;
    }

    public List<T> findAll(String redisHashKey, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
        Map<String, String> allCartItems = hashOperations.entries(redisHashKey);
        List<T> cartItems = new ArrayList<>();
        
        for (String json : allCartItems.values()) {
            T item = redisObjectMapper.readValue(json, clazz);
            cartItems.add(item);
        }
        return cartItems;
    }

    public boolean deleteById(String redisHashKey, K id) {
        hashOperations.delete(redisHashKey, String.valueOf(id));
        return true;
    }

    public boolean deleteByHashKey(String redisHashKey) {
        redisTemplate.delete(redisHashKey);
        return true;
    }

    public boolean existsById(String redisHashKey, K id) {
        return hashOperations.hasKey(redisHashKey, String.valueOf(id));
    }
}
