package com.watermelon.security.jwt;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
import com.watermelon.security.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${auth.jwt.secret}")
	public String SECRET_KEY ;
	@Value("${auth.jwt.expiration}")
	private int JWT_EXPIRATION;

	public String generateToken(CustomUserDetails userDetails) {
		 Date now = new Date();
	     Date timeExpiration = new Date(now.getTime() + JWT_EXPIRATION);
		
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(userDetails.getUsername())
				.issuer("watermelon.com")
				.issueTime(now)
				.expirationTime(timeExpiration)
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
	
	public String getUserNameFromToken(String token){
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			return signedJWT.getJWTClaimsSet().getSubject();
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
