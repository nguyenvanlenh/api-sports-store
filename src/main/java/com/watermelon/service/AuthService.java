package com.watermelon.service;

import com.watermelon.model.request.ForgotPasswordRequest;
import com.watermelon.model.request.LoginRequest;
import com.watermelon.model.request.RegisterRequest;
import com.watermelon.model.response.LoginResponse;

public interface AuthService {

	LoginResponse login(LoginRequest request);
	String register(RegisterRequest request);
	String forgotPassword(ForgotPasswordRequest request);
	
}
