package com.watermelon.dto.response;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record BrandResponse(int id, String name,String description, boolean isActive) implements Serializable {

}
