package com.watermelon.service.oauth2;

import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.model.enumeration.EDevice;

public interface OAuthStrategy {
    AuthenticationResponse authenticate(String code,EDevice device);
}

