package com.watermelon.dto.response;

import lombok.Builder;

@Builder
public record OrderStatusResponse (
	 Integer id,
	 String name){
}
