package com.watermelon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
@Component
@Getter
public class OAuthProperties {
    @Value("${outbound.gg.client-id}")
    private String googleClientId;

    @Value("${outbound.gg.client-secret}")
    private String googleClientSecret;

    @Value("${outbound.gg.redirect-uri}")
    private String googleRedirectUri;
    @Value("${outbound.gg.grant-type}")
    private String googleGrantType;

    @Value("${outbound.gh.client-id}")
    private String githubClientId;

    @Value("${outbound.gh.client-secret}")
    private String githubClientSecret;

    @Value("${outbound.gh.redirect-uri}")
    private String githubRedirectUri;
}
