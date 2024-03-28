package com.watermelon.model.dto.request;

public record RatingRequest(String content, int star,Long productId, Long userId) {

}
