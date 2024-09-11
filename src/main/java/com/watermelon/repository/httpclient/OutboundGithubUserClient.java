package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.watermelon.dto.response.oauth2.GithubUserResponse;

@FeignClient(name = "github-user-client", url = "https://api.github.com")
public interface OutboundGithubUserClient {
	@GetMapping(value = "/user")
    public GithubUserResponse getUserInfo(@RequestHeader("Authorization") String token);
}
