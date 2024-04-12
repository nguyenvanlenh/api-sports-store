package com.watermelon.dto.response;
import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse implements Serializable {
    private boolean authenticated;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private Set<String> listRoles;
}