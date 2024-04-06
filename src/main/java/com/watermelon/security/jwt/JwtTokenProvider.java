package com.watermelon.security.jwt;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.watermelon.dto.response.TokenResponse;
import com.watermelon.security.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secretKey}")
	public String SECRET_KEY ;
	@Value("${jwt.access.expiration}")
	private int JWT_ACCESS_EXPIRATION;
	@Value("${jwt.refresh.expiration}")
	private int JWT_REFRESH_EXPIRATION;
	
	@Autowired UserDetailsService userDetailsService;

	public String generateToken(String tokenType, CustomUserDetails userDetails) {
		 Date now = new Date();
	     Date timeExpiration = new Date(now.getTime() + JWT_ACCESS_EXPIRATION * 60 * 60);
	     
	     if(tokenType.equals("refreshToken"))
	    	 timeExpiration = new Date(now.getTime() + JWT_REFRESH_EXPIRATION * 60 * 60 * 24);
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(userDetails.getUsername())
				.issuer("watermelon.com")
				.issueTime(now)
				.expirationTime(timeExpiration)
				.claim("scope", buildScope(userDetails))
				.build();
		
		Payload payload = new Payload(jwtClaimsSet.toJSONObject());
		
		JWSObject jwsObject = new JWSObject(header, payload);
		
		try {
			jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			log.error("Cannot create token",e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private String buildScope(CustomUserDetails userDetails) {
		  StringJoiner stringJoiner = new StringJoiner(" ");
		  if(!CollectionUtils.isEmpty(userDetails.getAuthorities()))
			  userDetails.getAuthorities().forEach(authority -> {
				  stringJoiner.add(authority.getAuthority());
			  });
		return stringJoiner.toString();
	}

	public String getUserNameFromToken(String token){
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			return signedJWT.getJWTClaimsSet().getSubject();
		} catch (ParseException e) {
			log.error("Cannot parse token", e);
            throw new RuntimeException(e);
		}
	}
	
	public TokenResponse getRefreshToken(CustomUserDetails userDetails){
			String accessToken = generateToken("accessToken", userDetails);
			String refreshToken = generateToken("refreshToken", userDetails);
			Set<String> listRoles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
					.collect(Collectors.toSet());
			return TokenResponse.builder()
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.authenticated(true)
					.username(userDetails.getUsername())
					.userId(userDetails.getId())
					.listRoles(listRoles)
					.build();
		
	}
	public TokenResponse getAccessToken(String refreshToken){
		try {
			SignedJWT signedJWT = SignedJWT.parse(refreshToken);
			String username = signedJWT.getJWTClaimsSet().getSubject();
			CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username); 
			String accessToken = generateToken("accessToken", userDetails);
			Set<String> listRoles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
					.collect(Collectors.toSet());
			return TokenResponse.builder()
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.authenticated(true)
					.username(username)
					.userId(userDetails.getId())
					.listRoles(listRoles)
					.build();
		} catch (ParseException e) {
			log.error("Cannot parse token", e);
			throw new RuntimeException(e);
		}
	}
	
	public boolean validateToken(String token) {
		try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            // Xác thực chữ ký của token
            if (!signedJWT.verify(verifier) || expirationTime.before(new Date())) {
            	log.info("Token is expired");
                return false;
            }
            // Token hợp lệ
            return true;
        } catch (JOSEException | ParseException e) {
            log.error("Token validation failed", e);
            return false;
        }
	}
	
	
}
