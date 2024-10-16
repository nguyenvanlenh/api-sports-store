package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.PaymentRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.model.enumeration.EPaymentStatus;
import com.watermelon.service.PaymentService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

	PaymentService paymentService;

	@GetMapping
	public ResponseData<PageResponse<List<PaymentResponse>>> getPayments(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(@SortDefault(
					direction = Sort.Direction.ASC, 
					sort = {"createdOn" })) Pageable pageable) {
		PageResponse<List<PaymentResponse>> data = paymentService.getAllPayment(pageable);
		return ResponseData.<PageResponse<List<PaymentResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Payments data").data(data).build();
	}

	@GetMapping("/{paymentId}")
	public ResponseData<PaymentResponse> getPayment(@PathVariable Long paymentId) {
		PaymentResponse data = paymentService.getPaymentById(paymentId);
		return ResponseData.<PaymentResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Payment data")
				.data(data)
				.build();
	}

	@GetMapping("/users/{userId}")
	public ResponseData<PageResponse<List<PaymentResponse>>> getPaymentsByUserId(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(@SortDefault(
					direction = Sort.Direction.ASC, 
					sort = {"createdOn" })) Pageable pageable,
			@PathVariable Long userId) {
		PageResponse<List<PaymentResponse>> data = paymentService.getAllPaymentByUserId(userId, pageable);
		return ResponseData
				.<PageResponse<List<PaymentResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Payments data of each user")
				.data(data)
				.build();
	}

	@PatchMapping("/{paymentId}")
	public ResponseData<Void> updatePaymentStatus(
			@PathVariable Long paymentId,
			@RequestBody EPaymentStatus status) {
		paymentService.updatePaymentStatus(paymentId, status);
		return ResponseData.<Void>builder()
				.status(HttpStatus.OK.value())
				.message("Payment updated successfully")
				.build();
	}

	@PostMapping
	public ResponseData<Long> createPayment(@Valid @RequestBody PaymentRequest request) {

		Long data = paymentService.createPayment(request);
		return ResponseData.<Long>builder()
				.status(HttpStatus.OK.value())
				.message("Payment created successfully")
				.data(data)
				.build();
	}
}
