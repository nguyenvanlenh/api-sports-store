package com.watermelon.dto.response.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExchangeTokenGoogleResponse(
		String accessToken,
		Long expiresIn,
		String refreshToken,
		String scope,
		String grantType) {

}
