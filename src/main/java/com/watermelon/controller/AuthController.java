package com.watermelon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.event.RegistrationCompleteEvent;
import com.watermelon.model.dto.request.LoginRequest;
import com.watermelon.model.dto.request.RegisterRequest;
import com.watermelon.model.dto.response.LoginResponse;
import com.watermelon.model.entity.User;
import com.watermelon.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping("/register")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void register(@RequestBody @Valid RegisterRequest registerRequest, final HttpServletRequest servletRequest) {
		User user = authService.register(registerRequest);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(servletRequest)));
	}

	@PostMapping("/login")
	@ResponseStatus(code = HttpStatus.OK)
	public LoginResponse login(@RequestBody @Valid LoginRequest request) {
		return authService.login(request);
	}
	
	@GetMapping("/verifyEmail")
	@ResponseStatus(code = HttpStatus.OK)
    public String verifyEmail(@RequestParam("token") String token){
        return authService.verifyEmail(token);
    }
	

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
