package com.watermelon.service.imp;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.watermelon.dto.request.LoginRequest;
import com.watermelon.dto.request.RefreshRequest;
import com.watermelon.dto.request.RegisterRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.exception.RefreshTokenException;
import com.watermelon.exception.ResourceExistedException;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.exception.UserNotActivatedException;
import com.watermelon.model.entity.AuthToken;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.entity.VerificationToken;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.repository.AuthTokenRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.UserRolesRepository;
import com.watermelon.repository.VerificationTokenRepository;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.service.AuthService;
import com.watermelon.service.CommonService;
import com.watermelon.service.oauth2.OAuthStrategy;
import com.watermelon.utils.Constants;
import com.watermelon.utils.Constants.EmailVerificationMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements AuthService {

	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	JwtTokenProvider jwtTokenProvider;
	UserDetailsService userDetailsService;
	AuthenticationManager authenticationManager;
	VerificationTokenRepository tokenRepository;
	UserRolesRepository userRolesRepository;
	CommonService commonService;
	AuthTokenRepository authTokenRepository;
	Map<String, OAuthStrategy> oAuthStrategies;

	@Override
	public AuthenticationResponse outboundAuthenticate(String code, String type) {
        OAuthStrategy strategy = oAuthStrategies.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid OAuth type");
        }
        return strategy.authenticate(code);
    }

	@Transactional
	@Override
	public AuthenticationResponse login(LoginRequest request) {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.username(),
					request.password());
			Authentication authentication = authenticationManager.authenticate(token);
			CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService
					.loadUserByUsername(request.username());
			if(!customUserDetails.isActive())
				throw new UserNotActivatedException("User not active");
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String accessToken = jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, customUserDetails);
			String refreshToken = jwtTokenProvider.generateToken(Constants.REFRESH_TOKEN, customUserDetails);
			Set<String> listRoles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toSet());
			
			saveAuthToken(customUserDetails, refreshToken);
			log.info("User {} login success", request.username());
			
			
			return AuthenticationResponse.builder()
					.userId(customUserDetails.getId())
					.firstName(customUserDetails.getFirstName())
					.lastName(customUserDetails.getLastName())
					.email(customUserDetails.getEmail())
					.phone(customUserDetails.getPhone())
					.avatar(customUserDetails.getAvatar())
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.hasPassword(StringUtils.hasLength(customUserDetails.getPassword()))
					.listRoles(listRoles)
					.build();
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
			Role role =commonService.findRoleByName(ERole.USER.toString());
			setRoles.add(role);
		} else {
			listRoles.forEach(role -> {
				Role roleDetail = commonService.findRoleByName(role);
				setRoles.add(roleDetail);
			});
		}
		user.setListRoles(setRoles);

		return userRepository.save(user);
	}


	@Transactional
    public String verifyEmail(String token) {
        VerificationToken theToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(EmailVerificationMessage.EMAIL_NOTIFY_TOKEN_NOT_FOUND));

        if (theToken.getUser().isActive()) {
            return EmailVerificationMessage.EMAIL_NOTIFY_ACCOUNT_ALREADY_VERIFIED;
        }

        String validationMessage = validateVerificationToken(theToken);
        switch (validationMessage) {
            case EmailVerificationMessage.EMAIL_NOTIFY_VALID_TOKEN:
                activateUser(theToken);
                return EmailVerificationMessage.EMAIL_NOTIFY_SUCCESSFULLY_VERIFIED;
            case EmailVerificationMessage.EMAIL_NOTIFY_TOKEN_EXPIRED:
                handleExpiredToken(theToken);
                return EmailVerificationMessage.EMAIL_NOTIFY_TOKEN_EXPIRED;
            default:
                return EmailVerificationMessage.EMAIL_NOTIFY_INVALID_TOKEN;
        }
    }

    private void activateUser(VerificationToken token) {
        token.getUser().setActive(true);
        userRepository.save(token.getUser());
        tokenRepository.delete(token);
    }

    private void handleExpiredToken(VerificationToken token) {
        userRolesRepository.deleteUserRoleByUserId(token.getUser().getId());
        tokenRepository.delete(token);
        userRepository.deleteById(token.getUser().getId());
    }

    private String validateVerificationToken(VerificationToken token) {
        if (token == null) {
            return EmailVerificationMessage.EMAIL_NOTIFY_INVALID_TOKEN;
        }

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (token.getExpirationTime().getTime() <= currentTime) {
            return EmailVerificationMessage.EMAIL_NOTIFY_TOKEN_EXPIRED;
        }
        return EmailVerificationMessage.EMAIL_NOTIFY_VALID_TOKEN;
    }


	@Override
	public AuthenticationResponse getAccessTokenFromRefeshToken(RefreshRequest request) {
		String token = request.token();
		String accessToken = null;
		boolean isRevoked = 
				ObjectUtils.isEmpty(authTokenRepository.findByRefreshTokenAndRevokedFalse(token));
		if(jwtTokenProvider.validateToken(token) 
				&& !isRevoked 
				&& Constants.REFRESH_TOKEN.equals(jwtTokenProvider.getTypeToken(token))) {
			
			String username = jwtTokenProvider.getUsernameFromToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			accessToken = jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, userDetails);
			return AuthenticationResponse.builder()
					.accessToken(accessToken)
					.build();
		}else {
			throw new RefreshTokenException("Get access token fail");
		}
	}
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public boolean revokeRefreshToken(RefreshRequest request) {
		AuthToken authToken =  authTokenRepository.findByRefreshToken(request.token());
		if(jwtTokenProvider.validateToken(request.token()) && !ObjectUtils.isEmpty(authToken)) {
			authToken.setRevoked(true);
			authTokenRepository.save(authToken);
			return true;
		}else
			throw new RefreshTokenException("Revoked refresh token fail");
	}
	
	private void saveAuthToken(CustomUserDetails userDetails, String refreshToken) {
		
		List<AuthToken> listAuthTokens = authTokenRepository.findByUser_Id(userDetails.getId());
		
		if(!CollectionUtils.isEmpty(listAuthTokens))
			authTokenRepository.deleteAll(listAuthTokens);
		User user = commonService.findUserById(userDetails.getId());
		AuthToken authToken = AuthToken.builder()
				.refreshToken(refreshToken)
				.revoked(false)
				.user(user)
				.build();
		authTokenRepository.save(authToken);
		log.info("Add refresh token success");
		
	}
	

}
