package com.watermelon.dto.request;

import java.io.Serializable;

public record OrderAddressRequest(
		String addressLine1,
		String addressLine2,
		String city,
		String district,
		String province,
		String country
		) implements Serializable {

}
