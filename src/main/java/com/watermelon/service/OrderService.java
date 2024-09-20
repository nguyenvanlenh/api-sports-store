package com.watermelon.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.request.UpdateOrderStatusRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.model.enumeration.EDeliveryStatus;

public interface OrderService {

	PageResponse<List<OrderResponse>> getAllOrder(Pageable pageable);
	OrderResponse getOrderById(Long id);
	OrderResponse createOrder(OrderRequest orderRequest);
	void updateOrderStatus(UpdateOrderStatusRequest orderStatusRequest, Long idOrder);
	void updateDeliveryStatus(EDeliveryStatus deliveryStatus, Long idOrder);
	
	PageResponse<List<OrderResponse>> getOrderByUserId(Long idUser,Pageable pageable);
	
}
