package com.watermelon.controller;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${test.auth.accessToken}")
	private String accessToken;
	@Value("${test.auth.refreshToken}")
	private String refreshToken;
	@Value("${test.auth.accessTokenExpect}")
	private String tokenExpect;

	@BeforeEach
	public void initData() {
		registerRequest = new RegisterRequest("nguyenvanlenh", "12345678", "vanlenh2k@gmail.com", new ArrayList<>());
		loginRequest = new LoginRequest("nguyenlenh", "12345678");
		tokenResponse = TokenResponse.builder()
				.authenticated(true).userId(1L)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
		refreshRequest = new RefreshRequest(refreshToken);

	}

	@Test
	void login_ValidRequest_Success() throws Exception {
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
	void register_ValidRequest_Success() throws Exception {
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
	void login_InvalidRequestWithBlankPassword_Fail() throws Exception {
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
	void getAccessTokenFromRefeshToken_ValidRequest_Success() throws Exception {
		TokenResponse expect = TokenResponse.builder().accessToken(tokenExpect)
				.authenticated(true).build();
		Mockito.when(authService.getAccessTokenFromRefeshToken(ArgumentMatchers.any(RefreshRequest.class)))
				.thenReturn(expect);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refreshToken").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(refreshRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
				.andExpect(MockMvcResultMatchers.jsonPath("message").value("Refresh token"))
				.andExpect(MockMvcResultMatchers.jsonPath("data.accessToken").value(tokenExpect))
				.andExpect(MockMvcResultMatchers.jsonPath("data.authenticated").value(true)).andReturn();
	}
}
