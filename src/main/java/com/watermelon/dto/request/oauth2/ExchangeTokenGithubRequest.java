package com.watermelon.dto.request.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ExchangeTokenGithubRequest(
		String clientId,
		String clientSecret,
		String code,
		String redirectUri
		) {
}
