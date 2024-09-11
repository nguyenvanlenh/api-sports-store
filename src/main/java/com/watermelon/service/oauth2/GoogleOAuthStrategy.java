package com.watermelon.service.oauth2;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.watermelon.config.OAuthProperties;
import com.watermelon.dto.request.oauth2.ExchangeTokenGoogleRequest;
import com.watermelon.dto.response.AuthenticationResponse;
import com.watermelon.dto.response.oauth2.ExchangeTokenGoogleResponse;
import com.watermelon.dto.response.oauth2.GoogleUserResponse;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.model.enumeration.ETypeAccount;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.httpclient.OutboundGoogleUserClient;
import com.watermelon.repository.httpclient.OutboundIdentityGoogleClient;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.service.CommonService;
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service("GOOGLE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GoogleOAuthStrategy implements OAuthStrategy {

	OutboundIdentityGoogleClient googleClient;
	OutboundGoogleUserClient googleUserClient;
	OAuthProperties oauthProperties;
	JwtTokenProvider jwtTokenProvider;
	CommonService commonService;
	UserRepository userRepository;

    @Override
    public AuthenticationResponse authenticate(String code) {
        ExchangeTokenGoogleRequest request = new ExchangeTokenGoogleRequest(
                code, 
                oauthProperties.getGoogleClientId(), 
                oauthProperties.getGoogleClientSecret(), 
                oauthProperties.getGoogleRedirectUri(), 
                oauthProperties.getGoogleGrantType());
        ExchangeTokenGoogleResponse response = googleClient.exchangeToken(request);
        GoogleUserResponse userInfo = googleUserClient.getUserInfo("json", response.accessToken());

        Set<Role> setRoles = new HashSet<>();
        Role role = commonService.findRoleByName(ERole.USER.toString());
        setRoles.add(role);

        User user = userRepository.findByUsername(userInfo.email())
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(userInfo.email())
                        .email(userInfo.email())
                        .firstName(userInfo.givenName())
                        .lastName(userInfo.familyName())
                        .avatar(userInfo.picture())
                        .listRoles(setRoles)
                        .isActive(true)
                        .build()));

        CustomUserDetails customUserDetails = CustomUserDetails.mapUserToCustomUserDetail(user);
        String accessToken = jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, customUserDetails);
        String refreshToken = jwtTokenProvider.generateToken(Constants.REFRESH_TOKEN, customUserDetails);
        Set<String> listRoles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

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
                .typeAccount(ETypeAccount.GOOGLE)
                .build();
    }
}

