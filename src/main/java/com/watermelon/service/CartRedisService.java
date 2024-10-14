package com.watermelon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.watermelon.dto.CartItemDTO;
import com.watermelon.repository.RedisRepository;
import com.watermelon.utils.AuthenticationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartRedisService {
    RedisRepository<CartItemDTO, String> redisRepository;
    
    @NonFinal
    String CART_HASHKEY_PREFIX = "CART";

    public String addCartItem(CartItemDTO cartItem) {
        Long userId = AuthenticationUtils.extractUserId();
        String redisHashKey = String.format("%s_%s", CART_HASHKEY_PREFIX, userId);
        try {
            return redisRepository.save(redisHashKey, cartItem.id(), cartItem);
        } catch (JsonProcessingException e) {
            log.error("Error adding cart item with ID {}: {}", cartItem.id(), e.getMessage(), e);
            return null;
        }
    }

    public List<CartItemDTO> getCartItems() {
        Long userId = AuthenticationUtils.extractUserId();
        String redisHashKey = String.format("%s_%s", CART_HASHKEY_PREFIX, userId);
        try {
            return redisRepository.findAll(redisHashKey, CartItemDTO.class);
        } catch (JsonMappingException e) {
        	log.error("Error mapping cart items for user ID {}: {}", userId, e.getMessage(), e);
            return null;
        } catch (JsonProcessingException e) {
        	log.error("Error processing JSON for cart items of user ID {}: {}", userId, e.getMessage(), e);
            return null;
        }
    }

    public void deleteCartItem(String cartItemId) {
        Long userId = AuthenticationUtils.extractUserId();
        String redisHashKey = String.format("%s_%s", CART_HASHKEY_PREFIX, userId);
        try {
            redisRepository.deleteById(redisHashKey, cartItemId);
        } catch (Exception e) {
        	log.error("Error deleting cart item with ID {} for user ID {}: {}", cartItemId, userId, e.getMessage(), e);
        }
    }

    public void deleteAllCartItems() {
        Long userId = AuthenticationUtils.extractUserId();
        String redisHashKey = String.format("%s_%s", CART_HASHKEY_PREFIX, userId);
        try {
            redisRepository.deleteByHashKey(redisHashKey);
        } catch (Exception e) {
        	log.error("Error deleting all cart items for user ID {}: {}", userId, e.getMessage(), e);
        }
    }
}
