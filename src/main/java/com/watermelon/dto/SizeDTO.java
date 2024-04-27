package com.watermelon.dto;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record SizeDTO(int id, String name, int quantity)implements Serializable {
}
