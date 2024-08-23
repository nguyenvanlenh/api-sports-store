package com.watermelon.dto.response;

public record OrderAddressResponse(
		String addressLine,
		String city,
		String district,
		String province,
		String country
		){

}
