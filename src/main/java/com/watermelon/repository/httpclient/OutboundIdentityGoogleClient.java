package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import com.watermelon.dto.request.oauth2.ExchangeTokenRequest;
import com.watermelon.dto.response.oauth2.ExchangeTokenGoogleResponse;

import feign.QueryMap;

@FeignClient(name = "outbound-identity-google", url = "https://oauth2.googleapis.com")
public interface OutboundIdentityGoogleClient {
	@PostMapping(value="/token" ,produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ExchangeTokenGoogleResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
	

}
