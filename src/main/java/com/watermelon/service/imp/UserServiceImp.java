package com.watermelon.service.imp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.watermelon.dto.UserDTO;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.mapper.imp.UserMapper;
import com.watermelon.model.entity.User;
import com.watermelon.model.entity.VerificationToken;
import com.watermelon.repository.UserRepository;
import com.watermelon.repository.VerificationTokenRepository;
import com.watermelon.service.CommonService;
import com.watermelon.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {

	UserRepository userRepository;
	VerificationTokenRepository tokenRepository;
	CommonService commonService;
	
	@PreAuthorize("hasRole('ADMIN') || authentication.name == #username")
	@Override
	public UserDTO findByUsername(String username) {
		User user = commonService.findByUsername(username);
		return new UserMapper().toDTO(user);
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
	public Long saveOrUpdate(User user) {
		User userSaved = userRepository.save(user);
		return userSaved.getId();
	}

	@Override
	public void saveUserVerificationToken(User theUser, String token) {
		VerificationToken verificationToken = new VerificationToken(token, theUser);
		tokenRepository.save(verificationToken);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public PageResponse<List<UserDTO>> getAllUsers(Pageable pageable) {
		Page<User> pageUser = userRepository.findAll(pageable);
		List<UserDTO> lisUserDTOs = new UserMapper().toDTO(pageUser.getContent()); 
		return new PageResponse<List<UserDTO>>(
				pageUser.getPageable().getPageNumber(),
				pageUser.getSize(),
				pageUser.getTotalPages(),
				pageUser.getTotalElements(),
				lisUserDTOs);
	}

	@PostAuthorize("hasRole('ADMIN') || authentication.name == returnObject.username")
	@Override
	public UserDTO getUserById(Long id) {
		return  new UserMapper().toDTO(commonService.findUserById(id));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void updateStatusUser(Long idUser ,Boolean active) {
		User user = commonService.findUserById(idUser);
		user.setActive(active);
		userRepository.save(user);
	}

}
