package com.watermelon.dto.response.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GithubUserResponse(
		String login,
	    String id,
	    String avatarUrl,
	    String name,
	    String email
		) {

}
