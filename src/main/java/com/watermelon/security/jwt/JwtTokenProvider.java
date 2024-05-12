package com.watermelon.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.watermelon.utils.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${jwt.token.secretKey}")
	public String secretKey;
	@Value("${jwt.access.expiration}")
	private int jwtAccessExpiration;
	@Value("${jwt.refresh.expiration}")
	private int jwtRefreshExpiration;

	private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

	public String generateToken(String tokenType, UserDetails userDetails) {
		Date now = new Date();
		Date timeExpiration = new Date(now.getTime() + jwtAccessExpiration * 60 * 1000 );

		if (tokenType.equals(Constants.REFRESH_TOKEN))
			timeExpiration = new Date(now.getTime() + jwtRefreshExpiration * 60 * 60 * 24 * 1000);

		return Jwts.builder()
//				.setId(UUID.randomUUID().toString())
				.setSubject(userDetails.getUsername())
				.setIssuer("watermelon")
				.setIssuedAt(now)
				.setExpiration(timeExpiration)
				.claim("roles", buildClaimRoles(userDetails))
				.claim("typeToken", tokenType)
				.signWith(getSecretKey(), SignatureAlgorithm.HS512)
//				.setHeaderParam("typ", "JWT")
				.compact();
	}
	
	private String buildClaimRoles(UserDetails userDetails) {
		  StringJoiner stringJoiner = new StringJoiner(" ");
		  if(!CollectionUtils.isEmpty(userDetails.getAuthorities()))
			  userDetails.getAuthorities()
			  .forEach(authority ->
				  stringJoiner.add(authority.getAuthority())
			  );
		return stringJoiner.toString();
	}
	
	
	
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		if (claims != null) {
			return claimsResolver.apply(claims);
		}
		return null;
	}

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	public String getTypeToken(String token) {
		return getClaimFromToken(token, claims -> claims.get("typeToken", String.class));
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
	        throw new SignatureException("Invalid JWT signature");
	    } catch (MalformedJwtException e) {
	        throw new MalformedJwtException("Malformed JWT token");
	    } catch (UnsupportedJwtException e) {
	        throw new UnsupportedJwtException("Unsupported JWT token");
	    } catch (ExpiredJwtException e) {
	        throw new ExpiredJwtException(null, null, "JWT token is expired");
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("JWT token is empty or null");
	    }
	}

	
}
