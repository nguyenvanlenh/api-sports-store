package com.watermelon.dto.response;

public record OrderDetailResponse(
		Long idProduct,
		Integer quantity,
		Double price,
		Double discountAmount,
		String size,
		String categogy,
		String brand,
		String taxPercent){

}
