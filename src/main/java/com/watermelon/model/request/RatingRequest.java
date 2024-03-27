package com.watermelon.model.request;

public record RatingRequest(String content, int star,Long productId, Long userId) {

}
