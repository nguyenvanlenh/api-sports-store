package com.watermelon.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.dto.response.ResponseData;
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
		return ResponseData.<Long>builder()
				.status(HttpStatus.CREATED.value())
				.message("User registered successfully")
				.data(user.getId())
				.build();
	}

	@PostMapping("/login")

	public ResponseData<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
		AuthenticationResponse data = authService.login(request);
		return ResponseData.<AuthenticationResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Login successful")
				.data(data)
				.build();
	}

	@PostMapping("/refresh-token")
	public ResponseData<AuthenticationResponse> getRefreshToken(@RequestBody RefreshRequest request) {
			AuthenticationResponse data = authService.getAccessTokenFromRefeshToken(request);
			return ResponseData.<AuthenticationResponse>builder()
					.status(HttpStatus.OK.value())
					.message("Refresh token")
					.data(data)
					.build();
		
	}
	@PatchMapping("/refresh-token/revocation")
	public ResponseData<Void> revokeRefreshToken(@RequestBody RefreshRequest request) {
		authService.revokeRefreshToken(request);
		return ResponseData.<Void>builder()
				.status(HttpStatus.OK.value())
				.message("Refresh token revoked successfully")
				.build();
		
	}
	@PostMapping("/outbound/authentication")
    public ResponseData<AuthenticationResponse> outboundAuthenticate(
            @RequestParam("code") String code,
            @RequestParam("type") String type) {
        AuthenticationResponse data = authService.outboundAuthenticate(code, type);
        return ResponseData.<AuthenticationResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Login successful")
				.data(data)
				.build();
    }

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
