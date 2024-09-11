package com.watermelon.dto.response;

import java.math.BigDecimal;

import com.watermelon.dto.ProductDTO;

public record OrderDetailResponse(
		Long id,
		ProductDTO product,
		Integer quantity,
		BigDecimal price,
		Double discountAmount,
		String size,
		String categogy,
		String brand,
		Boolean isRating){

}
