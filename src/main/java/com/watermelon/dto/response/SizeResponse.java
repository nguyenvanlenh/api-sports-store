package com.watermelon.dto.response;

import lombok.Builder;

@Builder
public record SizeResponse(int id, String name, int quantity) {
}
