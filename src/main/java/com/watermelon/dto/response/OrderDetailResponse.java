package com.watermelon.dto.response;

import java.math.BigDecimal;

import com.watermelon.dto.ProductDTO;

public record OrderDetailResponse(
		ProductDTO product,
		Integer quantity,
		BigDecimal price,
		Double discountAmount,
		String size,
		String categogy,
		String brand){

}
