package com.watermelon.dto.request;

import java.io.Serializable;

public record RatingRequest(String content, int star, Long productId, Long userId) implements Serializable {

}
