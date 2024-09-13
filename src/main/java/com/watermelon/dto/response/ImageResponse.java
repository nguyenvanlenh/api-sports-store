package com.watermelon.dto.response;

import java.io.Serializable;

import lombok.Builder;
@Builder
public record ImageResponse(long id, String path)implements Serializable {
}
