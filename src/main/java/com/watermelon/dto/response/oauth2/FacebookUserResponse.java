package com.watermelon.dto.response.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FacebookUserResponse(
    String id,
    String name,
    String email,
    String firstName,
    String lastName,
    Picture picture) {

    public static record Picture(Data data) {
    }

    public static record Data(String url) {
    }
}
