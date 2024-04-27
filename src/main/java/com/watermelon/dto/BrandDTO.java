package com.watermelon.dto;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record BrandDTO(int id, String name) implements Serializable {

}
