package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, String> {

	boolean existsByProductIdAndProductQuantitySizeId(Long productId, Integer sizeId);
}
