package com.watermelon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.OrderStatus;
import com.watermelon.service.OrderService;
import com.watermelon.viewandmodel.request.OrderRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping()
	public List<Order> getAllOrder(){
		return orderService.getAllOrder();
	}
	@GetMapping("/{id}")
	public Order getOneOrderById(@PathVariable(name = "id") Long id) {
		return orderService.getOrderById(id);
	}
	@PostMapping()
	public ResponseEntity<Void> saveOrder(@RequestBody OrderRequest request){
		orderService.saveOrder(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PatchMapping("/{id}")
	public void updateStatus(@PathVariable(name = "id") Long id,@RequestBody OrderStatus status) {
		orderService.updateOrderStatus(status, id);
	}

}
