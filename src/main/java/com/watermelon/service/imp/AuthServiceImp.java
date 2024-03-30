package com.watermelon.service.imp;

import java.util.Calendar;
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
import org.springframework.transaction.annotation.Transactional;

import com.watermelon.exception.NotFoundException;
import com.watermelon.exception.ResourceExistedException;
import com.watermelon.model.dto.request.ChangePasswordRequest;
import com.watermelon.model.dto.request.ForgotPasswordRequest;
import com.watermelon.model.dto.request.LoginRequest;
import com.watermelon.model.dto.request.RegisterRequest;
import com.watermelon.model.dto.response.LoginResponse;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.entity.VerificationToken;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.repository.RoleRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.UserRolesRepository;
import com.watermelon.repository.VerificationTokenRepository;
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
	VerificationTokenRepository tokenRepository;
	UserRolesRepository userRolesRepository;
	
	private static String VALID_TOKEN = "valid";
	private static String INVALID_TOKEN = "Invalid verification token";
	private static String TOKEN_EXPIRED = "Token already expired";
	private static String ACCOUNT_VERIFIED = "This account has already been verified, please, login.";
	private static String SUCCESS_VERIFIED = "Email verified successfully. Now you can login to your account";
	

	@Transactional
	@Override
	public LoginResponse login(LoginRequest request) {
		// Thực hiện xác thực người dùng
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.username(),
				request.password());
		Authentication authentication = authenticationManager.authenticate(token);
		CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService
				.loadUserByUsername(request.username());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = jwtTokenProvider.generateToken(customUserDetails);
		Set<String> listRoles = customUserDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toSet());
		return new LoginResponse(jwtToken, true, listRoles);
	}

	@Transactional
	@Override
	public User register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.username()))
			throw new ResourceExistedException("username already exists!");
		if (userRepository.existsByEmail(request.email()))
			throw new ResourceExistedException("email already exists!");
		User user = new User();
		user.setUsername(request.username());
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setEmail(request.email());
		user.setActive(false);
		List<String> listRoles = request.listRoles();
		Set<Role> setRoles = new HashSet<>();
		if (listRoles.isEmpty()) {
			Role role = roleRepository.findByName(ERole.USER.toString())
					.orElseThrow(() -> new NotFoundException("Role not found!"));
			setRoles.add(role);
		} else {
			listRoles.forEach(role -> {
				Role roleDetail = roleRepository.findByName(role)
						.orElseThrow(() -> new NotFoundException("Role not found!"));
				setRoles.add(roleDetail);
			});
		}
		user.setListRoles(setRoles);

		User userRegistered = userRepository.save(user);
		return userRegistered;
	}

	@Override
	public String forgotPassword(ForgotPasswordRequest request) {

		return "";
	}

	@Override
	public String changePassword(ChangePasswordRequest request) {
		return null;
	}

	@Transactional
	@Override
	public String verifyEmail(String token) {
		VerificationToken theToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new NotFoundException("verification token not found"));

		if (theToken.getUser().isActive()) {
			return ACCOUNT_VERIFIED;
		}

		String validationMessage = validateVerificationToken(theToken);
		if (validationMessage.equalsIgnoreCase("valid")) {
			theToken.getUser().setActive(true);
			// update active user
			userRepository.save(theToken.getUser());
			// delete token valid
			tokenRepository.delete(theToken);
			return SUCCESS_VERIFIED;
		}
		return validationMessage;
	}

	public String validateVerificationToken(VerificationToken token) {
		if (token == null) {
			return INVALID_TOKEN;
		}

		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			// delete role default
			userRolesRepository.deleteUserRoleByUserId(token.getUser().getId());
			// delete token expired
			tokenRepository.delete(token);
			// delete user
			userRepository.deleteById(token.getUser().getId());
			return TOKEN_EXPIRED;
		}
		return VALID_TOKEN;
	}

	

}
