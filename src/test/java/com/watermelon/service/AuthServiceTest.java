package com.watermelon.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.TokenResponse;
import com.watermelon.exception.ResourceExistedException;
import com.watermelon.exception.UserNotActivatedException;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.repository.UserRepository;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.utils.Constants;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthServiceTest {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private CommonService commonService;

	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthService authService;

	private RegisterRequest registerRequest;
	private RefreshRequest refreshRequest;
	private LoginRequest loginRequest;
	private TokenResponse tokenResponse;

	private User user;

	@BeforeEach
	public void initData() {
		registerRequest = new RegisterRequest("nguyenvanlenh", "12345678", "vanlenh2k@gmail.com", new ArrayList<>());
		loginRequest = new LoginRequest("nguyenvanlenh", "12345678");
		tokenResponse = TokenResponse.builder()
				.authenticated(true)
				.userId(1L)
				.accessToken(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJhZmMwNzVkNi0yNGVjLTQ1MjItOTMzOC0xNmRmZGEwMGNhOTciLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxMzg1MTU5NCwicm9sZXMiOiIifQ.OYC7TrsQJFIvXxQeA5ciGms6fvXns1Y7607i3-sBxt5mUSmwf7hqQE5W8rKUux9u5JC3-7S2GJm_F4-KrraErA")
				.refreshToken(
						"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZjZlYzIyOC1iMDMwLTRjNGQtYmY4Yy1lZGYyMDcwNWFiOGYiLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxNTMyMTkyMywicm9sZXMiOiIifQ.Ndr6jMS9Hq1fp766yVuNZC_zQwt4ktqL2I_WsmoMn0uY3NjazyaxWSa6lCHbdWbBmJlkP1j6LUxKmkB5t3FnFA")
				.listRoles(new HashSet<>(List.of("ROLE_USER","READ")))
				.build();
		refreshRequest = new RefreshRequest(
				"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyZjZlYzIyOC1iMDMwLTRjNGQtYmY4Yy1lZGYyMDcwNWFiOGYiLCJzdWIiOiJuZ3V5ZW52YW5sZW5oMSIsImlzcyI6IndhdGVybWVsb24iLCJpYXQiOjE3MTM4NTA2OTQsImV4cCI6MTcxNTMyMTkyMywicm9sZXMiOiIifQ.Ndr6jMS9Hq1fp766yVuNZC_zQwt4ktqL2I_WsmoMn0uY3NjazyaxWSa6lCHbdWbBmJlkP1j6LUxKmkB5t3FnFA");
		user = User.builder().id(1L).username("nguyenvanlenh").password(passwordEncoder.encode("12345678"))
				.email("vanlenh2k@gmail.com").build();
	}
	@Test
	void login_ValidRequest_Success() {
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
		
		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(token)).thenReturn(authentication);

		
		CustomUserDetails userDetails = CustomUserDetails.builder()
				.id(1L)
				.username("nguyenvanlenh")
				.password("12345678")
				.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
				.build();
		when(userDetailsService.loadUserByUsername("nguyenvanlenh")).thenReturn(userDetails);
		
		
		when(jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, userDetails))
		.thenReturn(tokenResponse.getAccessToken());
		when(jwtTokenProvider.generateToken(Constants.REFRESH_TOKEN, userDetails))
		.thenReturn(tokenResponse.getRefreshToken());
		
		var response = authService.login(loginRequest);
		
		assertNotNull(response);
		assertThat(response.getAccessToken()).isEqualTo(tokenResponse.getAccessToken());
		assertThat(response.getRefreshToken()).isEqualTo(tokenResponse.getRefreshToken());
		assertThat(response.getUserId()).isEqualTo(1L);
		assertThat(response.getListRoles()).contains("ROLE_USER");
	}
	
	@Test
	void login_UsernameOrPasswordInvalid_ThrowBadcredentialsException() {
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
		
		when(authenticationManager.authenticate(token))
		.thenThrow(BadCredentialsException.class);
		assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(token));
	}
	@Test
	void login_UsernameI_ThrowUserNotActivatedException() {
		UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(token))
		.thenReturn(authentication);
		
		when(userDetailsService.loadUserByUsername("nguyenvanlenh"))
		.thenThrow(UserNotActivatedException.class);

		assertThrows(UserNotActivatedException.class, () -> userDetailsService.loadUserByUsername("nguyenvanlenh"));
	}

	@Test
	void register_ValidRequest_Success() {
        when(userRepository.existsByUsername("nguyenvanlenh")).thenReturn(false);
        when(userRepository.existsByEmail("vanlenh2k@gmail.com")).thenReturn(false);

        Role userRole = new Role();
        userRole.setId(1);
        userRole.setName("USER");
        when(commonService.findRoleByName("USER")).thenReturn(userRole);
        
        when(userRepository.save(any(User.class))).thenReturn(user);

        var response = authService.register(registerRequest);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("nguyenvanlenh");
        assertTrue(passwordEncoder.matches("12345678", response.getPassword()));
        assertThat(response.getEmail()).isEqualTo("vanlenh2k@gmail.com");
    }

	@Test
	void register_UsernameExists_ThrowResourceExistedException() {
		String existingUsername = "nguyenvanlenh";
		when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

		assertThrows(ResourceExistedException.class, () -> {
			authService.register(registerRequest);
		});
	}

	@Test
	void register_EmailExists_ThrowResourceExistedException() {
		String existingEmail = "vanlenh2k@gmail.com";
		when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

		assertThrows(ResourceExistedException.class, () -> {
			authService.register(registerRequest);
		});
	}

	@Test
	void getAccessTokenFromRefeshToken_ValidRequest_Success() {
		RefreshRequest request = mock(RefreshRequest.class);
		when(request.token()).thenReturn(tokenResponse.getRefreshToken());
		
		when(jwtTokenProvider.validateToken(tokenResponse.getRefreshToken())).thenReturn(true);
		when(jwtTokenProvider.getUsernameFromToken(tokenResponse.getRefreshToken())).thenReturn("nguyenvanlenh");

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetailsService.loadUserByUsername("nguyenvanlenh")).thenReturn(userDetails);
		
		when(jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, userDetails)).thenReturn(tokenResponse.getAccessToken());
		
		
		var response = authService.getAccessTokenFromRefeshToken(request);
		
		assertNotNull(response);
	    assertTrue(response.getAuthenticated());
	    assertThat(response.getAccessToken()).isEqualTo(tokenResponse.getAccessToken());
	}

}
