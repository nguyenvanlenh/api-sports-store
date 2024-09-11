package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watermelon.dto.response.oauth2.GoogleUserResponse;

@FeignClient(name = "outbound-user-client", url = "https://www.googleapis.com")
public interface OutboundGoogleUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    GoogleUserResponse getUserInfo(@RequestParam("alt") String alt,
                                     @RequestParam("access_token") String accessToken);
}
