package com.watermelon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.model.request.LoginRequest;
import com.watermelon.model.request.RegisterRequest;
import com.watermelon.model.response.LoginResponse;
import com.watermelon.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
@Autowired private AuthService authService;
	
	@PostMapping("/register")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void register(@RequestBody @Valid RegisterRequest request) {
		authService.register(request);
	}
	
	@PostMapping("/login")
	@ResponseStatus(code = HttpStatus.OK)
	public LoginResponse login(@RequestBody @Valid LoginRequest request) {
		return authService.login(request);
	}	
	

    
}
