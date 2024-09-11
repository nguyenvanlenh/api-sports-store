package com.watermelon.dto;

import lombok.Builder;

@Builder
public record SizeDTO(int id, String name, int quantity) {
}
