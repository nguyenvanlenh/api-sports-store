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
import org.springframework.web.bind.annotation.RequestParam;
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
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

	PaymentService paymentService;
	
	@GetMapping
	public ResponseData<PageResponse<List<PaymentResponse>>> getPayments(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.ASC, sort = {"createdOn" })
					) Pageable pageable){
		PageResponse<List<PaymentResponse>> data = paymentService.getAllPayment(pageable);
		return new ResponseData<PageResponse<List<PaymentResponse>>>(
				HttpStatus.OK.value(), 
				"Payment data list", 
				data);
	}
	@GetMapping("/{paymentId}")
	public ResponseData<PaymentResponse> getPayment(@PathVariable Long paymentId){
		PaymentResponse data = paymentService.getPaymentById(paymentId);
		return new ResponseData<PaymentResponse>(
				HttpStatus.OK.value(), 
				"Payment data", 
				data);
	}
	@GetMapping("/user/{userId}")
	public ResponseData<PageResponse<List<PaymentResponse>>> getPaymentsByUserId(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.ASC, sort = {"createdOn" })
					) Pageable pageable,
			@PathVariable Long userId){
		PageResponse<List<PaymentResponse>> data = paymentService.getAllPaymentByUserId(userId,pageable);
		return new ResponseData<PageResponse<List<PaymentResponse>>>(
				HttpStatus.OK.value(), 
				"Payment data of each user", 
				data);
	}
	@PatchMapping("/{paymentId}")
	public ResponseData<Void> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam EPaymentStatus status){
		paymentService.updatePaymentStatus(paymentId, status);
		return new ResponseData<Void>(
				HttpStatus.ACCEPTED.value(), 
				"Payment updated successfully");
	}
	@PostMapping
	public ResponseData<Long> createPayment(@Valid @RequestBody PaymentRequest request){
		
		Long data = paymentService.createPayment(request);
		return new ResponseData<Long>(
				HttpStatus.ACCEPTED.value(),
				"Payment added successfully",
				data
				);
	}
}
