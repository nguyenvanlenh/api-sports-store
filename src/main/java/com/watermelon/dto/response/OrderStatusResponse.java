package com.watermelon.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record OrderStatusResponse (
	 Integer id,
	 String name){
}
