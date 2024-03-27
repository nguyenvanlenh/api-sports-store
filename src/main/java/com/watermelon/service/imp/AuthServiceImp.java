package com.watermelon.service.imp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.model.request.ForgotPasswordRequest;
import com.watermelon.model.request.LoginRequest;
import com.watermelon.model.request.RegisterRequest;
import com.watermelon.model.response.LoginResponse;
import com.watermelon.repository.RoleRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.service.AuthService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	RoleRepository roleRepository;
	JwtTokenProvider jwtTokenProvider;
	UserDetailsService userDetailsService;
	AuthenticationManager authenticationManager;

	@Override
	public LoginResponse login(LoginRequest request) {
		// Thực hiện xác thực người dùng
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.username(),
				request.password());
		Authentication authentication = authenticationManager.authenticate(token);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService
				.loadUserByUsername(request.username());
		String jwtToken = jwtTokenProvider.generateToken(customUserDetails);
		Set<String> listRoles = customUserDetails.getAuthorities().stream()
				.map(authority -> authority.getAuthority())
				.collect(Collectors.toSet());
		return new LoginResponse(
				jwtToken,
				true,
				listRoles);
	}

	@Override
	public String register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.username()))
			throw new RuntimeException("username already exists!");
		if (userRepository.existsByEmail(request.email()))
			throw new RuntimeException("email already exists!");
		User user = new User();
		user.setUsername(request.username());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setEmail(request.email());
		user.setActive(true);
		List<String> listRoles = request.listRoles();
		Set<Role> setRoles = new HashSet<>();
		if (listRoles.isEmpty()) {
			Role role = roleRepository.findByName(ERole.USER.toString())
					.orElseThrow(() -> new RuntimeException("Role not found!"));
			setRoles.add(role);
		} else {
			listRoles.forEach(role -> {
				Role roleDetail = roleRepository.findByName(role)
						.orElseThrow(() -> new RuntimeException("Role not found!"));
				setRoles.add(roleDetail);
			});
		}
		user.setListRoles(setRoles);

		userRepository.save(user);
		return "Register success";
	}

	@Override
	public String forgotPassword(ForgotPasswordRequest request) {

		return "";
	}

}
