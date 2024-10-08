package com.watermelon.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RecaptchaResponse(
		boolean success, 
		String challengeTs, 
		String hostname, List<String> errorCodes) {

}
