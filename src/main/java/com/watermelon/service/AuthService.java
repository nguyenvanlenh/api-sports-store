package com.watermelon.service;

import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.model.entity.User;

public interface AuthService {

	AuthenticationResponse outboundAuthenticate(String code, String type);
	AuthenticationResponse login(LoginRequest request);
	User register(RegisterRequest request);
	String verifyEmail(String token);
	AuthenticationResponse getAccessTokenFromRefeshToken(RefreshRequest request);
	boolean revokeRefreshToken(RefreshRequest request);
}
