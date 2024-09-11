package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.watermelon.dto.request.oauth2.ExchangeTokenGithubRequest;
import com.watermelon.dto.response.oauth2.ExchangeTokenGithubResponse;

import feign.QueryMap;

@FeignClient(name = "githubClient", url = "https://github.com")
public interface OutboundIdentityGithubClient {

    @PostMapping(value = "/login/oauth/access_token", 
                 headers = "Accept=application/json")
    public ExchangeTokenGithubResponse exchangeToken(@QueryMap ExchangeTokenGithubRequest request);

    
}

