package com.watermelon.model.dto.request;

public record OrderDetailRequest(
		Long idProduct,
		Integer quantity,
		Double price,
		Double discountAmount,
		Integer size,
		Integer categogy,
		Integer brand,
		String taxPercent) {

}
