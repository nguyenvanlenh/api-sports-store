package com.watermelon.mapper.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.watermelon.dto.response.OrderDetailResponse;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.UserResponse;
import com.watermelon.mapper.EntityMapper;
import com.watermelon.model.entity.Order;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMapper implements EntityMapper<OrderResponse, Order> {
	OrderAdressMapper orderAdressMapper;
	OrderDetailMapper orderDetailMapper;
	PaymentMapper paymentMapper;
	
	@Override
	public OrderResponse toDTO(Order entity) {
	    return Optional.ofNullable(entity)
	        .map(order -> {
	            List<OrderDetailResponse> orderDetailResponses = 
	            		orderDetailMapper.toDTO(order.getListDetails()
	            				.stream().toList());
	            return OrderResponse.builder()
	                .id(order.getId())
	                .user(UserResponse.builder()
	                    .id(order.getUser().getId())
	                    .username(order.getUser().getUsername())
	                    .build())
	                .payment(paymentMapper.toDTO(order.getPayment()))
	                .address(orderAdressMapper.toDTO(order.getOrderAddress()))
	                .nameCustomer(order.getNameCustomer())
	                .emailCustomer(order.getEmailCustomer())
	                .phoneNumberCustomer(order.getPhoneNumberCustomer())
	                .totalPrice(order.getTotalPrice())
	                .deliveryFee(order.getDeliveryFee())
	                .orderStatus(order.getOrderStatus())
	                .deliveryStatus(order.getDeliveryStatus())
	                .deliveryMethod(order.getDeliveryMethod())
	                .coupondCode(order.getCouponCode())
	                .rejectReason(order.getRejectReason())
	                .listOrderDetails(orderDetailResponses)
	                .createdOn(order.getCreatedOn())
	                .build();
	        })
	        .orElse(OrderResponse.builder().build());
	}




}
