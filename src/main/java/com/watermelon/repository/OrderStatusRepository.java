package com.watermelon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.OrderStatus;


public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer>{

	Optional<OrderStatus> findByName(String name);
}
