package com.watermelon.dto.response;

import java.math.BigDecimal;

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
