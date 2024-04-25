package com.watermelon.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.dto.response.TokenResponse;
import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.model.entity.User;
import com.watermelon.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

	AuthService authService;
	ApplicationEventPublisher publisher;

	@Transactional
	@PostMapping("/register")
	public ResponseData<Long> register(@RequestBody @Valid RegisterRequest registerRequest,
			final HttpServletRequest servletRequest) {
		User user = authService.register(registerRequest);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(servletRequest)));
		return new ResponseData<>(HttpStatus.CREATED.value(), "User registered successfully", user.getId());
	}

	@PostMapping("/login")

	public ResponseEntity<ResponseData<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
		TokenResponse data = authService.login(request);
		ResponseData<TokenResponse> response = new ResponseData<>(HttpStatus.ACCEPTED.value(), "Login successful",
				data);
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(data.getAccessToken());
		return ResponseEntity.ok().headers(headers).body(response);
	}

	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("token") String token) {
		return authService.verifyEmail(token);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<ResponseData<TokenResponse>> getRefreshToken(@RequestBody RefreshRequest request) {
			TokenResponse data = authService.getAccessTokenFromRefeshToken(request);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(data.getAccessToken());
			return ResponseEntity.ok().headers(headers)
					.body(new ResponseData<>(HttpStatus.OK.value(), "Refresh token", data));
		
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
