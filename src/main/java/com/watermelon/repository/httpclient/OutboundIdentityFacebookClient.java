package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.watermelon.dto.request.oauth2.ExchangeTokenRequest;
import com.watermelon.dto.response.oauth2.ExchangeTokenFacebookResponse;

import feign.QueryMap;

@FeignClient(name="outbound-identity-facebook", url="https://graph.facebook.com/v20.0/oauth")
public interface OutboundIdentityFacebookClient {
	
	@GetMapping(value = "/access_token")
	public ExchangeTokenFacebookResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
	

}
