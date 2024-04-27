package com.watermelon.service;

import java.util.List;

import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.model.entity.DeliveryStatus;
import com.watermelon.model.entity.OrderStatus;

public interface OrderService {

	List<OrderResponse> getAllOrder();
	OrderResponse getOrderById(Long id);
	Long createOrder(OrderRequest orderRequest);
	void updateOrderStatus(OrderStatus orderStatus, Long idOrder);
	void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long idOrder);
	
}
