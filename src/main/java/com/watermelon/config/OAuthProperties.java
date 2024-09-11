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
    
    @Value("${outbound.fb.client-id}")
    private String facebookClientId;
    
    @Value("${outbound.fb.client-secret}")
    private String facebookClientSecret;
    
    @Value("${outbound.fb.redirect-uri}")
    private String facebookRedirectUri;
    
    @Value("${outbound.fb.fields}")
    private String facebookFieldsInfo;
}
