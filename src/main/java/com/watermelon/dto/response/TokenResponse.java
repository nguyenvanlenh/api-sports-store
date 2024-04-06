package com.watermelon.dto.response;
import java.io.Serializable;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse implements Serializable {
    private boolean authenticated;
    private String username;
    private long userId;
    private String accessToken;
    private String refreshToken;
    private Set<String> listRoles;
}