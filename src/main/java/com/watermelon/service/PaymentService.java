package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.request.PaymentRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.model.enumeration.EPaymentStatus;

public interface PaymentService {
	PageResponse<List<PaymentResponse>> getAllPayment(Pageable pageable);
	PageResponse<List<PaymentResponse>> getAllPaymentByUserId(Long userId, Pageable pageable);
	PaymentResponse getPaymentById(Long paymentId);
	Long createPayment(PaymentRequest request);
	void updatePaymentStatus(Long paymentId,EPaymentStatus status);
}
