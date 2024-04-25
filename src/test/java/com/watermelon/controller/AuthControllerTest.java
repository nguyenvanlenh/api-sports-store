package com.watermelon.controller;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.TokenResponse;
import com.watermelon.model.entity.User;
import com.watermelon.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AuthService authService;

	@MockBean
	private ApplicationEventPublisher publisher;

	private RegisterRequest registerRequest;
	private RefreshRequest refreshRequest;
	private LoginRequest loginRequest;
	private TokenResponse tokenResponse;

	@BeforeEach
	public void initData() {
		registerRequest = new RegisterRequest("nguyenvanlenh", "12345678", "vanlenh2k@gmail.com", new ArrayList<>());
		loginRequest = new LoginRequest("nguyenlenh", "12345678");
		tokenResponse = TokenResponse.builder()
				.authenticated(true).userId(1L)
				.accessToken(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhZmMwNzVkNi0yNGVjLTQ1MjItOTMzOC0xNmRmZGEwMGNhOTciLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxMzg1MTU5NCwicm9sZXMiOiIifQ.OYC7TrsQJFIvXxQeA5ciGms6fvXns1Y7607i3-sBxt5mUSmwf7hqQE5W8rKUux9u5JC3-7S2GJm_F4-KrraErA")
				.refreshToken(
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZjZlYzIyOC1iMDMwLTRjNGQtYmY4Yy1lZGYyMDcwNWFiOGYiLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxNTMyMTkyMywicm9sZXMiOiIifQ.Ndr6jMS9Hq1fp766yVuNZC_zQwt4ktqL2I_WsmoMn0uY3NjazyaxWSa6lCHbdWbBmJlkP1j6LUxKmkB5t3FnFA")
				.build();
		refreshRequest = new RefreshRequest(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZjZlYzIyOC1iMDMwLTRjNGQtYmY4Yy1lZGYyMDcwNWFiOGYiLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxNTMyMTkyMywicm9sZXMiOiIifQ.Ndr6jMS9Hq1fp766yVuNZC_zQwt4ktqL2I_WsmoMn0uY3NjazyaxWSa6lCHbdWbBmJlkP1j6LUxKmkB5t3FnFA");

	}

	@Test
	void login_ValidRequest_Success() throws JsonProcessingException, Exception {
		TokenResponse expect = tokenResponse;

		Mockito.when(authService.login(ArgumentMatchers.any(LoginRequest.class)))
		.thenReturn(expect);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(202))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("Login successful"))
				.andExpect(MockMvcResultMatchers.jsonPath("data.userId").value(1L))
				.andExpect(MockMvcResultMatchers.jsonPath("data.accessToken").value(tokenResponse.getAccessToken()))
				.andExpect(MockMvcResultMatchers.jsonPath("data.refreshToken").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("data.authenticated").value(true)).andReturn();
	}

	@Transactional
	@Test
	void register_ValidRequest_Success() throws JsonProcessingException, Exception {
		User expect = User.builder()
				.id(1L)
				.username("nguyenvanlenh")
				.password(passwordEncoder.encode("12345678"))
				.build();

		Mockito.when(authService.register(ArgumentMatchers.any(RegisterRequest.class)))
		.thenReturn(expect);

		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(201))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("User registered successfully"))
				.andExpect(MockMvcResultMatchers.jsonPath("data").value(expect.getId())).andReturn();
	}

	@Test
	void login_InvalidRequestWithBlankPassword_Fail() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest("nguyenlenh", "");
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("password cannot be blank")).andReturn();
	}

	@WithMockUser(username = "admin", roles = { "ADMIN", "USER" })
	@Test
	void getAccessTokenFromRefeshToken_ValidRequest_Success() throws JsonProcessingException, Exception {
		TokenResponse expect = TokenResponse.builder().accessToken(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZjZlYzIyOC1iMDMwLTRjNGQtYmY4Yy1lZGYyMDcwNWFiOGYiLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxNTMyMTkyMywicm9sZXMiOiIifQ.Ndr6jMS9Hq1fp766yVuNZC_zQwt4ktqL2I_WsmoMn0uY3NjazyaxWSa6lCHbdWbBmJlkP1j6LUxKmkB5t3FnFA")
				.authenticated(true).build();
		Mockito.when(authService.getAccessTokenFromRefeshToken(ArgumentMatchers.any(RefreshRequest.class)))
				.thenReturn(expect);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refreshToken").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refreshRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("Refresh token"))
				.andExpect(MockMvcResultMatchers.jsonPath("data.accessToken").value(tokenResponse.getRefreshToken()))
				.andExpect(MockMvcResultMatchers.jsonPath("data.authenticated").value(true)).andReturn();
	}
}
