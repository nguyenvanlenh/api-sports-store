package com.watermelon.dto.request;

public record OrderAddressRequest(
		String addressLine,
		String commune,
		String district,
		String province,
		String country
		){

}
