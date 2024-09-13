package com.watermelon.service.imp;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watermelon.dto.request.OrderAddressRequest;
import com.watermelon.dto.request.OrderDetailRequest;
import com.watermelon.dto.request.OrderRequest;
import com.watermelon.dto.response.OrderResponse;
import com.watermelon.dto.response.PageResponse;
import com.watermelon.exception.ForbiddenException;
import com.watermelon.mapper.imp.OrderMapper;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Category;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.OrderAddress;
import com.watermelon.model.entity.OrderDetail;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Size;
import com.watermelon.model.entity.User;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;
import com.watermelon.repository.OrderAddressRepository;
import com.watermelon.repository.OrderDetailRepository;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.CommonService;
import com.watermelon.service.OrderService;
import com.watermelon.service.ProductService;
import com.watermelon.utils.AuthenticationUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImp implements OrderService {

	ProductService productService;
	OrderRepository orderRepository;
	OrderDetailRepository orderDetailRepository;
	OrderAddressRepository orderAddressRepository;
	SizeRepository sizeRepository;
	CommonService commonService;
	OrderMapper orderMapper;
	
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional(readOnly = true)
	@Override
	public PageResponse<List<OrderResponse>> getAllOrder(Pageable pageable) {
		Page<Order> page = orderRepository.findAll(pageable);
		return  new PageResponse<>(
				page.getPageable().getPageNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements(),
				orderMapper.toDTO(page.getContent())
				);
	}

    @PostAuthorize("hasRole('ADMIN') || authentication.name == returnObject.user.username")
    @Transactional(readOnly = true)
	@Override
	public OrderResponse getOrderById(Long id) {
		return orderMapper.toDTO(commonService.findOrderById(id));
	}

	@Transactional
	@Override
	public OrderResponse createOrder(OrderRequest orderRequest) {
		Order order = mapRequestToOrder(orderRequest);
		OrderAddress orderAddress = mapRequestToOrderAddress(orderRequest.address());
		OrderAddress orderAddressSaved = orderAddressRepository.save(orderAddress);

		order.setOrderAddress(orderAddressSaved);
		
		User user = commonService.findUserById(AuthenticationUtils.extractUserId());
		order.setUser(user);
		
		Order orderSaved = orderRepository.save(order);

		saveOrderDetails(orderRequest.listOrderDetails(), orderSaved);
		log.info("Order ID {} added successfully",orderSaved.getId());
		return orderMapper.toDTO(orderSaved);
	}

	@Transactional
	@Override
	public void updateOrderStatus(EOrderStatus orderStatus, Long idOrder) {
		Order order = commonService.findOrderById(idOrder);
		if (EOrderStatus.CANCELLED.equals(order.getOrderStatus())) {
			log.error("Cannot update order status as this order has been cancelled!");
			throw new ForbiddenException("Cannot update order status as this order has been cancelled!");
		}
		if (EOrderStatus.CANCELLED.toString().equals(orderStatus)) {
			order.getListDetails().forEach(orderDetail -> {
				List<Size> sizes = sizeRepository.findByName(orderDetail.getSize());
				productService.updateProductQuantityForSize(-orderDetail.getQuantity(), idOrder, sizes.get(0).getId());
			});
		}
		order.setOrderStatus(orderStatus);
		orderRepository.save(order);
	}

	@Transactional
	@Override
	public void updateDeliveryStatus(EDeliveryStatus deliveryStatus, Long idOrder) {
		Order order = commonService.findOrderById(idOrder);
		if (EOrderStatus.CANCELLED.equals(order.getOrderStatus())) {
			log.error("Cannot update delivery status as this order has been cancelled!");
			throw new ForbiddenException("Cannot update delivery status as this order has been cancelled!");
		}
		if (EDeliveryStatus.DELIVERED.equals(order.getDeliveryStatus())) {
			log.error("Cannot update delivery status as this order has been delivered!");
			throw new ForbiddenException("Cannot update delivery status as this order has been delivered!");
		}
		order.setDeliveryStatus(deliveryStatus);
		orderRepository.save(order);
		log.info("updated delivery status successfully to {} ", deliveryStatus);
	}
	
	@PreAuthorize("hasRole('ADMIN') || #userId == authentication.principal.id")
	@Override
	public PageResponse<List<OrderResponse>> getOrderByUserId(Long userId, Pageable pageable) {
		
		Page<Order> page = orderRepository.findByUser_Id(userId, pageable);
		return  new PageResponse<>(
				page.getPageable().getPageNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements(),
				orderMapper.toDTO(page.getContent())
				);
	}

	private OrderDetail mapRequestToOrderDetail(OrderDetailRequest request) {

		OrderDetail orderDetail = new OrderDetail();

		Product product = commonService.findProductById(request.idProduct());
		orderDetail.setProduct(product);
		orderDetail.setQuantity(request.quantity());
		orderDetail.setPrice(request.price());

		Brand brand = commonService.findBrandProductById(request.brand());
		orderDetail.setBrand(brand.getName());

		Category category = commonService.findCategoryProductById(request.category());
		orderDetail.setCategogy(category.getName());

		Size size = commonService.findSizeProductById(request.size());

		orderDetail.setSize(size.getName());

		orderDetail.setDiscountAmount(request.discountAmount());

		return orderDetail;
	}

	private OrderAddress mapRequestToOrderAddress(OrderAddressRequest request) {

		OrderAddress orderAddress = new OrderAddress();
		orderAddress.setAddressLine(request.addressLine());
		orderAddress.setCommune(request.commune());
		orderAddress.setDistrict(request.district());
		orderAddress.setProvince(request.province());
		orderAddress.setCountry(request.country());
		return orderAddress;
	}

	private Order mapRequestToOrder(OrderRequest request) {
		Order order = new Order();
		order.setNameCustomer(request.nameCustomer());
		order.setEmailCustomer(request.emailCustomer());
		order.setPhoneNumberCustomer(request.phoneNumberCustomer());
		order.setTotalPrice(request.totalPrice());
		order.setDeliveryFee(request.deliveryFee());
		order.setCouponCode(request.coupondCode());
		order.setRejectReason(request.rejectReason());

		order.setOrderStatus(request.orderStatus());

		order.setDeliveryMethod(request.deliveryMethod());

		order.setDeliveryStatus(request.deliveryStatus());

		return order;
	}

	private void updateQuantityProduct(OrderDetail orderDetail, OrderDetailRequest orderDetailRequest) {
		productService.updateProductQuantityForSize(orderDetail.getQuantity(), orderDetail.getProduct().getId(),
				orderDetailRequest.size());
	}

	private void saveOrderDetails(Set<OrderDetailRequest> listOrderDetails, Order orderSaved) {
		listOrderDetails.forEach(orderDetailRq -> {
			OrderDetail orderDetail = mapRequestToOrderDetail(orderDetailRq);

			updateQuantityProduct(orderDetail, orderDetailRq);

			orderDetail.setOrder(orderSaved);
			orderDetailRepository.save(orderDetail);
		});
	}
}
