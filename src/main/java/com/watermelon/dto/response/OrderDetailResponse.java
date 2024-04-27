package com.watermelon.dto.response;

import java.io.Serializable;

public record OrderDetailResponse(
		Long idProduct,
		Integer quantity,
		Double price,
		Double discountAmount,
		String size,
		String categogy,
		String brand,
		String taxPercent) implements Serializable {

}
