package com.watermelon.dto.request;

public record RatingRequest(String content, int star,Long productId, Long userId) {

}
