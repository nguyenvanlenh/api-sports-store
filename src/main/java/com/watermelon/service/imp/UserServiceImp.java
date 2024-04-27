package com.watermelon.service.imp;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.watermelon.model.entity.User;
import com.watermelon.model.entity.VerificationToken;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.VerificationTokenRepository;
import com.watermelon.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService{

	UserRepository userRepository;
	
	VerificationTokenRepository tokenRepository;
	
	@PostAuthorize("hasRole('ADMIN') || authentication.name == returnObject.username")
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("Username not found"));
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User saveOrUpdate(User user) {
		return userRepository.save(user);
	}
	@Override
	public void saveUserVerificationToken(User theUser, String token) {
		VerificationToken verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
		
	}

}
