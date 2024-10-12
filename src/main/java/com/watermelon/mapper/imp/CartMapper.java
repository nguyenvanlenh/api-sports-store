package com.watermelon.mapper.imp;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.CartItemDTO;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.CartItem;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartMapper implements EntityMapper<CartItemDTO, CartItem>{
	
	SizeMapper sizeMapper;
	ProductMapper productMapper;
	
	public CartItemDTO toDTO(CartItem entity) {
		return Optional.ofNullable(entity)
				.map(item -> CartItemDTO.builder()
						.id(item.getId())
						.quantity(item.getQuantity())
						.size(sizeMapper.toDTO(entity.getProductQuantity()))
						.product(productMapper.toDTO(entity.getProduct()))
						.build()
				)
				.orElse(CartItemDTO.builder().build());
	}
	

}
