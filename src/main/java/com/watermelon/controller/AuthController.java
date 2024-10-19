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

import com.watermelon.dto.request.ForgotPasswordRequest;
import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.request.UpdatePasswordRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.exception.RecaptchaTokenInvalidException;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.EDevice;
import com.watermelon.service.AuthService;
import com.watermelon.service.RecaptchaService;
import com.watermelon.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {

	AuthService authService;
	ApplicationEventPublisher publisher;
	RecaptchaService recaptchaService;
	
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

	public ResponseData<AuthenticationResponse> login(
			@RequestBody @Valid LoginRequest request,
			final HttpServletRequest servletRequest) {
		boolean recaptchaVerified = recaptchaService.verifyRecaptcha(request.recaptchaToken());
		EDevice device = getDevice(servletRequest);
		if(device.equals(EDevice.TEST))
			recaptchaVerified = true;
        if (!recaptchaVerified) {
        	throw new  RecaptchaTokenInvalidException("Invalid reCAPTCHA");
        }
		AuthenticationResponse data = authService.login(request, getDevice(servletRequest));
		log.info("User login success {}", data);
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
            @RequestParam("type") String type,
            final HttpServletRequest servletRequest) {
        AuthenticationResponse data = authService.outboundAuthenticate(code, type, getDevice(servletRequest));
        return ResponseData.<AuthenticationResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Login successful")
				.data(data)
				.build();
    }
	@PostMapping("/logout")
	public ResponseData<Void> logout(final HttpServletRequest servletRequest) {
		authService.logout(getDevice(servletRequest));
	    return ResponseData.<Void>builder()
	            .status(HttpStatus.OK.value())
	            .message("Logout successful")
	            .build();
	}
	@PostMapping("/forgot-password")
    public ResponseData<Void> forgotPassword(
    		@RequestBody @Valid ForgotPasswordRequest request,
    		final HttpServletRequest servletRequest) {
		boolean recaptchaVerified = recaptchaService.verifyRecaptcha(request.recaptchaToken());
		EDevice device = getDevice(servletRequest);
		if(device.equals(EDevice.TEST))
			recaptchaVerified = true;
        if (!recaptchaVerified) {
        	throw new RecaptchaTokenInvalidException("Invalid reCAPTCHA");
        }
        authService.forgotPassword(request);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Password reset email sent successfully")
                .build();
    }

    @PatchMapping("/update-password")
    public ResponseData<Void> updatePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        authService.updatePassword(request);
        return ResponseData.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Password updated successfully")
                .build();
    }
	
	private String applicationUrl(HttpServletRequest request) {
		String scheme = request.getScheme(); //scheme HTTP/HTTPS
		return scheme + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	private EDevice getDevice(HttpServletRequest servletRequest) {
	    String userAgent = servletRequest.getHeader("User-Agent");
	    System.out.println(userAgent);
	    if (userAgent != null) {
	        String userAgentLower = userAgent.toLowerCase();
	        if (userAgentLower.contains("postman")) 
	            return EDevice.TEST;
	        if (userAgentLower.contains("swagger")) 
	            return EDevice.TEST; 
	        if (userAgentLower.contains(Constants.MOBILE)) 
	            return EDevice.MOBILE;
	    }

	    return EDevice.OTHER;
	}
}
