package com.watermelon.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.request.UpdateOrderStatusRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
	
	@GetMapping
	public ResponseData<PageResponse<List<OrderResponse>>> getOrders(
			@PageableDefault(page = 0, size = 20) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.DESC, sort = {"id" })
					) Pageable pageable
			){
		return ResponseData.<PageResponse<List<OrderResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Orders data")
				.data(orderService.getAllOrder(pageable))
				.build();
	}
	@GetMapping("/{orderId}")
	public ResponseData<OrderResponse> getOrderById(
			@Min(value = 1, message = "Order ID must be greater than or equal to 1")
			@PathVariable Long orderId) {
		return ResponseData.<OrderResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Order data")
				.data(orderService.getOrderById(orderId))
				.build();
	}
	@PostMapping()
	public ResponseData<OrderResponse> saveOrder(@RequestBody @Valid OrderRequest request){
		OrderResponse data = orderService.createOrder(request);
		return ResponseData.<OrderResponse>builder()
				.status(HttpStatus.CREATED.value())
				.message("Order added successfully")
				.data(data)
				.build();
	}
	
	@PatchMapping("/{orderId}")
	public ResponseData<Void> updateOrderStatus(
			@PathVariable Long orderId,
			@RequestBody UpdateOrderStatusRequest request) {
		orderService.updateOrderStatus(request, orderId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message("Order updated successfully")
				.build();
	}
	@PatchMapping("/{orderId}/delivery-status")
	public ResponseData<Void> updateDeliveryStatus(
			@PathVariable Long orderId,
			@RequestBody EDeliveryStatus status) {
		orderService.updateDeliveryStatus(status, orderId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.ACCEPTED.value())
				.message(String.format("Delivery status of order id %d updated successfully",orderId))
				.build();
	}
	
	@GetMapping("/users/{userId}")
	public ResponseData<PageResponse<List<OrderResponse>>> getOrdersOfUserId(
			@PathVariable Long userId,
			@PageableDefault(page = 0, size = 100) 
			@SortDefaults(
					@SortDefault(direction = Sort.Direction.DESC, sort = {"id" })
					) Pageable pageable
			){
		return ResponseData.<PageResponse<List<OrderResponse>>>builder()
				.status(HttpStatus.OK.value())
				.message("Orders data")
				.data(orderService.getOrderByUserId(userId,pageable))
				.build();
	}

	@DeleteMapping("/{orderId}")
	public ResponseData<Void> getOrdersOfUserId(
			@PathVariable Long orderId){
		orderService.deleteOrder(orderId);
		return ResponseData.<Void>builder()
				.status(HttpStatus.NO_CONTENT.value())
				.message("Orders data")
				.build();
	}
}
