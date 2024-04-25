package com.watermelon.dto.request;

import java.io.Serializable;

public record OrderDetailRequest(
		Long idProduct,
		Integer quantity,
		Double price,
		Double discountAmount,
		Integer size,
		Integer categogy,
		Integer brand,
		String taxPercent) implements Serializable {

}
