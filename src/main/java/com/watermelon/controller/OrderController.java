package com.watermelon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.model.entity.OrderStatus;
import com.watermelon.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class OrderController {
	
	OrderService orderService;
	
	@GetMapping()
	public ResponseData<List<OrderResponse>> getOrders(){
		return new ResponseData<>(HttpStatus.OK.value(),"Data orders",orderService.getAllOrder());
	}
	@GetMapping("/{id}")
	public ResponseData<OrderResponse> getOrderById(
			@Min(value = 1, message = "Order ID must be greater than or equal to 1")
			@PathVariable(name = "id") Long id) {
		return  new ResponseData<>(HttpStatus.OK.value(),"Data order",orderService.getOrderById(id));
	}
	@PostMapping()
	public ResponseData<Long> saveOrder(@RequestBody @Valid OrderRequest request){
		return new ResponseData<>(
				HttpStatus.CREATED.value(),
				"Order added successfully",
				orderService.createOrder(request));
	}
	
	@PatchMapping("/{id}")
	public ResponseData<Void> updateStatus(
			@Min(value = 1, message = "Order ID must be greater than or equal to 1")
			@NotBlank(message = "PathVariable Order ID is required")
			@PathVariable(name = "id") Long id
			,@NotBlank(message = "Status is required") @RequestParam String status) {
		orderService.updateOrderStatus(status, id);
		return new ResponseData<>(HttpStatus.ACCEPTED.value(),"Order updated successfully");
	}

}
