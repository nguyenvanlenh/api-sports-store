package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watermelon.dto.response.oauth2.FacebookUserResponse;

@FeignClient(name = "facebook-client", url = "https://graph.facebook.com/me")
public interface OutboundFacebookUserClient {
	@PostMapping("")
	public FacebookUserResponse userInfo(
			@RequestParam("fields") String fields,
			@RequestParam("access_token") String accessToken);

}
