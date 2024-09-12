package com.watermelon.service.imp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.watermelon.dto.PaymentRequest;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.PaymentResponse;
import com.watermelon.exception.ForbiddenException;
import com.watermelon.mapper.imp.PaymentMapper;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.Payment;
import com.watermelon.model.enumeration.EPaymentStatus;
import com.watermelon.repository.PaymentRepository;
import com.watermelon.service.CommonService;
import com.watermelon.service.PaymentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentServiceImp implements PaymentService{
	CommonService commonService;
	PaymentRepository paymentRepository;
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public PageResponse<List<PaymentResponse>> getAllPayment(Pageable pageable) {
		Page<Payment> pagePayments = paymentRepository.findAll(pageable);
		List<PaymentResponse> data = PaymentMapper.getInstance().toDTO(pagePayments.getContent());
		return new PageResponse<List<PaymentResponse>>(
				pagePayments.getNumber(), 
				pagePayments.getSize(), 
				pagePayments.getTotalPages(),
				pagePayments.getTotalElements(),
				data);
	}

	@PostAuthorize("hasRole('ADMIN') || returnObject.order.user.id == authentication.principal.id")
	@Override
	public PaymentResponse getPaymentById(Long paymentId) {
		Payment payment = commonService.findPaymentById(paymentId);
		return PaymentMapper.getInstance().toDTO(payment);
	}
	@PreAuthorize("hasRole('USER')")
	@Override
	public Long createPayment(PaymentRequest request) {
		Order order = commonService.findOrderById(request.orderId());
		Payment payment = Payment.builder()
		.order(order)
		.amount(request.amount())
		.paymentFee(request.paymentFee())
		.paymentMethod(request.paymentMethod())
		.paymentStatus(request.paymentStatus())
		.build();
		Payment paymentSaved = paymentRepository.save(payment);
		log.info("Payment created with ID: {}", paymentSaved.getId());
		return paymentSaved.getId();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void updatePaymentStatus(Long paymentId, EPaymentStatus status) {
		Payment payment = commonService.findPaymentById(paymentId);
		if(EPaymentStatus.CANCELLED.equals(payment.getPaymentStatus())) {
			log.error("Cannot update payment status as this order has been cancelled!");
		throw new ForbiddenException("Cannot update payment status as this order has been cancelled!");
		}
		payment.setPaymentStatus(status);
		log.info("Payment status updated successfully for payment ID: {}", paymentId);
	}
	@PreAuthorize("hasRole('USER') || #userId == authentication.principal.id")
	@Override
	public PageResponse<List<PaymentResponse>> getAllPaymentByUserId(Long userId, Pageable pageable) {
		Page<Payment> pagePayments = paymentRepository.findPaymentByOrderUserId(userId,pageable);
		List<PaymentResponse> data = PaymentMapper.getInstance().toDTO(pagePayments.getContent());
		return new PageResponse<List<PaymentResponse>>(
				pagePayments.getNumber(), 
				pagePayments.getSize(), 
				pagePayments.getTotalPages(),
				pagePayments.getTotalElements(),
				data);
	}

}
