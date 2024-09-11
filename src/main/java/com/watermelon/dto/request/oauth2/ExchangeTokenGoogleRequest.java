package com.watermelon.dto.request.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExchangeTokenGoogleRequest(
		String code,
		String clientId,
		String clientSecret,
		String redirectUri,
		String grantType) {
}
