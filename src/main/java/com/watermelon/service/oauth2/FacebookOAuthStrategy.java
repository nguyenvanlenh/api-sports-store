package com.watermelon.service.oauth2;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.watermelon.config.OAuthProperties;
import com.watermelon.dto.request.oauth2.ExchangeTokenRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.dto.response.oauth2.ExchangeTokenFacebookResponse;
import com.watermelon.dto.response.oauth2.FacebookUserResponse;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.model.enumeration.ETypeAccount;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.httpclient.OutboundFacebookUserClient;
import com.watermelon.repository.httpclient.OutboundIdentityFacebookClient;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.service.CommonService;
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service("FACEBOOK")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FacebookOAuthStrategy implements OAuthStrategy {
	OAuthProperties oAuthProperties;
	JwtTokenProvider jwtTokenProvider;
	CommonService commonService;
	UserRepository userRepository;
	OutboundIdentityFacebookClient facebookClient;
	OutboundFacebookUserClient facebookUserClient;
	
	@Override
	public AuthenticationResponse authenticate(String code) {
		
		ExchangeTokenFacebookResponse response = facebookClient.exchangeToken(
				ExchangeTokenRequest.builder()
				.clientId(oAuthProperties.getFacebookClientId())
				.clientSecret(oAuthProperties.getFacebookClientSecret())
				.redirectUri(oAuthProperties.getFacebookRedirectUri())
				.code(code)
				.build()
				);
		
		FacebookUserResponse userInfo = facebookUserClient.userInfo(
				oAuthProperties.getFacebookFieldsInfo(),
				response.accessToken());
		
		Set<Role> setRoles = new HashSet<>();
        Role role = commonService.findRoleByName(ERole.USER.toString());
        setRoles.add(role);

        User user = userRepository.findByUsername(String.valueOf(userInfo.id()))
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(String.valueOf(userInfo.id()))
                        .email(userInfo.email())
                        .firstName(userInfo.firstName())
                        .lastName(userInfo.lastName())
                        .avatar(userInfo.picture().data().url())
                        .listRoles(setRoles)
                        .isActive(true)
                        .build()));

        CustomUserDetails customUserDetails = CustomUserDetails.mapUserToCustomUserDetail(user);
        String accessToken = jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, customUserDetails);
        String refreshToken = jwtTokenProvider.generateToken(Constants.REFRESH_TOKEN, customUserDetails);
        
        commonService.saveAuthToken(customUserDetails.getId(),refreshToken);
        
        Set<String> listRoles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .userId(customUserDetails.getId())
                .avatar(customUserDetails.getAvatar())
                .firstName(customUserDetails.getFirstName())
                .lastName(customUserDetails.getLastName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .hasPassword(StringUtils.hasLength(customUserDetails.getPassword()))
                .listRoles(listRoles)
                .typeAccount(ETypeAccount.FACEBOOK)
                .build();
	}

}
