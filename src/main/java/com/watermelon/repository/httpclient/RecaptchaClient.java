package com.watermelon.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watermelon.dto.response.RecaptchaResponse;

@FeignClient(name = "recaptchaClient", url = "https://www.google.com/recaptcha/api")
public interface RecaptchaClient {
	@PostMapping("/siteverify")
    RecaptchaResponse verify(@RequestParam("secret") String secret,
                             @RequestParam("response") String response);
	
}
