package com.watermelon.dto;

import com.watermelon.dto.response.ProductResponse;
import com.watermelon.dto.response.SizeResponse;

import lombok.Builder;
@Builder
public record CartItemDTO(
		String id,
		SizeResponse size,
		int quantity,
		ProductResponse product
		) {

}
