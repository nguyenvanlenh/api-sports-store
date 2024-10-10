package com.watermelon.dto.response;

import lombok.Builder;

@Builder
public record VNPayResponse(
		String code,
		String status,
		String url
		) {

}
