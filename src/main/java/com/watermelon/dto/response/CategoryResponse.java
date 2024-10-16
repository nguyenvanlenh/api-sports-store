package com.watermelon.dto.response;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record CategoryResponse(int id, String name,String description, boolean isActive)implements  Serializable {
}
