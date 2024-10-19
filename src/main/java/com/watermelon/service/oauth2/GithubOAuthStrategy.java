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
import com.watermelon.dto.response.oauth2.ExchangeTokenGithubResponse;
import com.watermelon.dto.response.oauth2.GithubUserResponse;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.EDevice;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.model.enumeration.ETypeAccount;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.httpclient.OutboundGithubUserClient;
import com.watermelon.repository.httpclient.OutboundIdentityGithubClient;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.security.jwt.JwtTokenProvider;
import com.watermelon.service.CommonService;
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service("GITHUB")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GithubOAuthStrategy implements OAuthStrategy {

    OutboundIdentityGithubClient githubClient;
    OutboundGithubUserClient githubUserClient;
    OAuthProperties oauthProperties;
    JwtTokenProvider jwtTokenProvider;
    CommonService commonService;
    UserRepository userRepository;

    @Override
    public AuthenticationResponse authenticate(String code,EDevice device) {
    
        ExchangeTokenGithubResponse response = githubClient.exchangeToken(
        		ExchangeTokenRequest.builder()
        		.clientId(oauthProperties.getGithubClientId())
        		.clientSecret(oauthProperties.getGithubClientSecret())
        		.code(code)
        		.redirectUri(oauthProperties.getGithubRedirectUri())
        		.build()
        		);
        String token = "Bearer " + response.accessToken();
        GithubUserResponse userInfo = githubUserClient.getUserInfo(token);

        Set<Role> setRoles = new HashSet<>();
        Role role = commonService.findRoleByName(ERole.USER.toString());
        setRoles.add(role);

        User user = userRepository.findByUsername(userInfo.login())
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(userInfo.login())
                        .email(userInfo.email())
                        .firstName(userInfo.name())
                        .avatar(userInfo.avatarUrl())
                        .listRoles(setRoles)
                        .isActive(true)
                        .build()));

        CustomUserDetails customUserDetails = CustomUserDetails.mapUserToCustomUserDetail(user);
        String accessToken = jwtTokenProvider.generateToken(Constants.ACCESS_TOKEN, customUserDetails);
        String refreshToken = jwtTokenProvider.generateToken(Constants.REFRESH_TOKEN, customUserDetails);
        
        commonService.saveAuthToken(customUserDetails.getId(),refreshToken,device);
        
        Set<String> listRoles = customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .userId(customUserDetails.getId())
                .avatar(customUserDetails.getAvatar())
                .firstName(customUserDetails.getFirstName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .hasPassword(StringUtils.hasLength(customUserDetails.getPassword()))
                .listRoles(listRoles)
                .typeAccount(ETypeAccount.GITHUB)
                .build();
    }
}

