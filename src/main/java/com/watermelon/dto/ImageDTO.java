package com.watermelon.dto;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record ImageDTO(long id, String path)implements Serializable {
}
