package com.watermelon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	Page<Payment> findPaymentByOrderUserId(Long userId, Pageable pageable);

}
