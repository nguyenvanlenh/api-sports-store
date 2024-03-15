package com.watermelon.service;

import java.util.List;

import com.watermelon.model.entity.DeliveryStatus;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.OrderStatus;
import com.watermelon.model.request.OrderRequest;

public interface OrderService {

	List<Order> getAllOrder();
	Order getOrderById(Long id);
	Order saveOrder(OrderRequest orderRequest);
	void updateOrderStatus(OrderStatus orderStatus, Long idOrder);
	void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long idOrder);
	
}
