package com.watermelon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.watermelon.dto.response.RecaptchaResponse;
import com.watermelon.repository.httpclient.RecaptchaClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecaptchaService {

	@NonFinal
	@Value("${recaptcha.secret-key}")
    String recaptchaSecretKey;

    RecaptchaClient recaptchaClient;
    
    public boolean verifyRecaptcha(String token) {
        RecaptchaResponse recaptchaResponse = recaptchaClient.verify(recaptchaSecretKey, token);
        return recaptchaResponse.success();
    }
}
