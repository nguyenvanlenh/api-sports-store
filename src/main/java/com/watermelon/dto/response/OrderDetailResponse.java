package com.watermelon.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
@Builder
public record OrderDetailResponse(
		Long id,
		ProductResponse product,
		Integer quantity,
		BigDecimal price,
		Double discountAmount,
		String size,
		String categogy,
		String brand,
		Boolean isRating){

}
