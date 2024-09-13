package com.watermelon.dto.response;

import java.time.ZonedDateTime;

public record RatingResponse(
		Long id,
		String content,
		int star,
		String urlAvatar,
		String nameCustomer,
		ZonedDateTime createdOn){

}
