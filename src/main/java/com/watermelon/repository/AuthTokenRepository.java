package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.AuthToken;
import com.watermelon.model.enumeration.EDevice;

import java.util.List;


public interface AuthTokenRepository extends JpaRepository<AuthToken, String>{
	
	AuthToken findByRefreshTokenAndRevokedFalse(String refreshToken); 
	AuthToken findByRefreshToken(String refreshToken);
	AuthToken findByUserIdAndDevice(Long userId, EDevice device);
	List<AuthToken> findByUserId(Long userId);
	

}
