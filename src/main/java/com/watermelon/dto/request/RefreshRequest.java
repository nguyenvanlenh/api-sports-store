package com.watermelon.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank(message = "Token must not be blank") String token){

}
