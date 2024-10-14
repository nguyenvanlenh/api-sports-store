package com.watermelon.event.listener;

import org.springframework.stereotype.Component;

import com.watermelon.dto.CartItemDTO;
import com.watermelon.mapper.imp.CartMapper;
import com.watermelon.model.entity.CartItem;
import com.watermelon.service.CartRedisService;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemListener {
	
	CartRedisService cartRedisService;
	CartMapper cartMapper;
	
	@PostPersist
	public void postSaveCartItem(CartItem cartItem) {
		CartItemDTO dto = cartMapper.toDTO(cartItem);
		cartRedisService.addCartItem(dto);
	}
	
	@PostUpdate
	public void postUpdateCartItem(CartItem cartItem) {
		cartRedisService.deleteCartItem(cartItem.getId());
		CartItemDTO dto = cartMapper.toDTO(cartItem);
		cartRedisService.addCartItem(dto);
	}
	@PostRemove
	public void postRemoveCartItem(CartItem cartItem) {
		cartRedisService.deleteCartItem(cartItem.getId());
	}

}
