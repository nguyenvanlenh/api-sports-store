package com.watermelon.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public record RatingDTO(
		Long id,
		String content,
		int star,
		Long productId,
		Long userId,
		ZonedDateTime createdOn) implements Serializable {

}
