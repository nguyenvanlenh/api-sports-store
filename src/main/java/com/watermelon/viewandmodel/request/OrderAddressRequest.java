package com.watermelon.viewandmodel.request;

public record OrderAddressRequest(
		String addressLine1,
		String addressLine2,
		String city,
		String district,
		String province,
		String country
		) {

}
