package com.watermelon.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.model.entity.AuthToken;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Category;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.Payment;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.Size;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.repository.AuthTokenRepository;
import com.watermelon.repository.BrandRepository;
import com.watermelon.repository.CategoryRepository;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.PaymentRepository;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.RatingRepository;
import com.watermelon.repository.RoleRepository;
import com.watermelon.repository.SizeRepository;
import com.watermelon.repository.UserRepository;
import com.watermelon.utils.AuthenticationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonService {
	ProductRepository productRepository;
	OrderRepository orderRepository;
	SizeRepository sizeRepository;
	BrandRepository brandRepository;
	CategoryRepository categoryRepository;
	RatingRepository ratingRepository;
	UserRepository userRepository;
	RoleRepository roleRepository;
	PaymentRepository paymentRepository;
	AuthTokenRepository authTokenRepository;

	public Order findOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", id));
	}

	public Product findProductById(Long id) {
		if (AuthenticationUtils.extractUserAuthorities()
				.contains(String.format("ROLE_%s", ERole.ADMIN.toString())))
			return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", id));
		return productRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new ResourceNotFoundException("PRODUCT_NOT_FOUND", id));
	}

	public Size findSizeProductById(Integer id) {
		return sizeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SIZE_NOT_FOUND", id));
	}

	public Brand findBrandProductById(Integer id) {
		return brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("BRAND_NOT_FOUND", id));
	}

	public Category findCategoryProductById(Integer id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CATEGORY_NOT_FOUND", id));
	}

	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", userId));
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}

	public Role findRoleByName(String name) {
		return roleRepository.findByName(ERole.USER.toString())
				.orElseThrow(() -> new ResourceNotFoundException("ROLE_NOT_FOUND", name));
	}

	public Rating findRatingById(Long ratingId) {
		return ratingRepository.findById(ratingId)
				.orElseThrow(() -> new ResourceNotFoundException("RATING_NOT_FOUND", ratingId));
	}
	
	public Payment findPaymentById(Long paymentId) {
		return paymentRepository.findById(paymentId)
				.orElseThrow(()-> new ResourceNotFoundException("PAYMENT_NOT_FOUND", paymentId));
	}
	
	public void saveAuthToken(Long userId, String refreshToken) {
		List<AuthToken> listAuthTokens = authTokenRepository.findByUserId(userId);
		if(!CollectionUtils.isEmpty(listAuthTokens))
			authTokenRepository.deleteAll(listAuthTokens);
		User user = findUserById(userId);
		AuthToken authToken = AuthToken.builder()
				.refreshToken(refreshToken)
				.revoked(false)
				.user(user)
				.build();
		authTokenRepository.save(authToken);
		
	}

}
