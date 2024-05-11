package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.model.entity.DeliveryStatus;

public interface OrderService {

	PageResponse<List<OrderResponse>> getAllOrder(Pageable pageable);
	OrderResponse getOrderById(Long id);
	Long createOrder(OrderRequest orderRequest);
	void updateOrderStatus(String status, Long idOrder);
	void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long idOrder);
	
	PageResponse<List<OrderResponse>> getOrderByUserId(Long idUser,Pageable pageable);
	
}
