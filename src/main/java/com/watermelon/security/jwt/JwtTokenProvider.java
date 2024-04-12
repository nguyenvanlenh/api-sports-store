package com.watermelon.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	public String SECRET_KEY;
	@Value("${jwt.access.expiration}")
	private int JWT_ACCESS_EXPIRATION;
	@Value("${jwt.refresh.expiration}")
	private int JWT_REFRESH_EXPIRATION;
	
	@Autowired UserDetailsService userDetailsService;

	private Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

	public String generateToken(String tokenType, UserDetails userDetails) {
		Date now = new Date();
		Date timeExpiration = new Date(now.getTime() + JWT_ACCESS_EXPIRATION * 60 * 60);

		if (tokenType.equals(Constants.REFRESH_TOKEN))
			timeExpiration = new Date(now.getTime() + JWT_REFRESH_EXPIRATION * 60 * 60 * 24 * 1000);

		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.setSubject(userDetails.getUsername())
				.setIssuer("watermelon.com")
				.setIssuedAt(now)
				.setExpiration(timeExpiration)
				.claim("roles", buildClaimRoles(userDetails))
				.signWith(getSecretKey(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	private String buildClaimRoles(UserDetails userDetails) {
		  StringJoiner stringJoiner = new StringJoiner(" ");
		  if(!CollectionUtils.isEmpty(userDetails.getAuthorities()))
			  userDetails.getAuthorities()
			  .forEach(authority -> {
				  stringJoiner.add(authority.getAuthority());
			  });
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
	        log.error("Invalid JWT signature: {}", e.getMessage());
	        throw new SignatureException("Invalid JWT signature");
	    } catch (MalformedJwtException e) {
	        log.error("Malformed JWT token: {}", e.getMessage());
	        throw new MalformedJwtException("Malformed JWT token");
	    } catch (UnsupportedJwtException e) {
	        log.error("Unsupported JWT token: {}", e.getMessage());
	        throw new UnsupportedJwtException("Unsupported JWT token");
	    } catch (ExpiredJwtException e) {
	        log.error("JWT token is expired: {}", e.getMessage());
	        throw new ExpiredJwtException(null, null, "JWT token is expired");
	    } catch (IllegalArgumentException e) {
	        log.error("JWT token is empty or null: {}", e.getMessage());
	        throw new IllegalArgumentException("JWT token is empty or null");
	    }
	}

	
}
