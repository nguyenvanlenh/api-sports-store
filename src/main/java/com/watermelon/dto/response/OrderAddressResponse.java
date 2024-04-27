package com.watermelon.dto.response;

import java.io.Serializable;

public record OrderAddressResponse(
		String addressLine1,
		String addressLine2,
		String city,
		String district,
		String province,
		String country
		) implements Serializable {

}
