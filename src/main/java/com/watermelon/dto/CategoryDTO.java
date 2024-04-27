package com.watermelon.dto;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record CategoryDTO(int id, String name)implements  Serializable {
}
