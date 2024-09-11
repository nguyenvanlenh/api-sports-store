package com.watermelon.service.oauth2;

import com.watermelon.dto.response.AuthenticationResponse;

public interface OAuthStrategy {
    AuthenticationResponse authenticate(String code);
}

