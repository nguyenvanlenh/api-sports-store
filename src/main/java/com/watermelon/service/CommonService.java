package com.watermelon.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Category;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Rating;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.Size;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.ERole;
import com.watermelon.repository.BrandRepository;
import com.watermelon.repository.CategoryRepository;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.RatingRepository;
import com.watermelon.repository.RoleRepository;
import com.watermelon.repository.SizeRepository;
import com.watermelon.repository.UserRepository;

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

	public Order findOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", id));
	}

	public Product findProductById(Long id) {
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

	public User findUserById(long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", id));
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}

	public Role findRoleByName(String name) {
		return roleRepository.findByName(ERole.USER.toString())
				.orElseThrow(() -> new ResourceNotFoundException("ROLE_NOT_FOUND", name));
	}

	public Rating findRatingById(long id) {
		return ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RATING_NOT_FOUND", id));
	}

}
