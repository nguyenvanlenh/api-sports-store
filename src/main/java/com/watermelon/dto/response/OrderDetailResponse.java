package com.watermelon.dto.response;

import java.math.BigDecimal;

public record OrderDetailResponse(
		Long idProduct,
		Integer quantity,
		BigDecimal price,
		Double discountAmount,
		String size,
		String categogy,
		String brand){

}
