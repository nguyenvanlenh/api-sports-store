package com.watermelon.dto.response;

import lombok.Builder;

@Builder
public record OrderAddressResponse(
		String addressLine,
		String commune,
		String district,
		String province,
		String country
		){

}
