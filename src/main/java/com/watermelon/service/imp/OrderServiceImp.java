package com.watermelon.service.imp;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watermelon.dto.request.OrderAddressRequest;
import com.watermelon.dto.request.OrderDetailRequest;
import com.watermelon.dto.request.OrderRequest;
import com.watermelon.exception.ForbiddenException;
import com.watermelon.exception.NotFoundException;
import com.watermelon.model.entity.Brand;
import com.watermelon.model.entity.Category;
import com.watermelon.model.entity.DeliveryMethod;
import com.watermelon.model.entity.DeliveryStatus;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.OrderAddress;
import com.watermelon.model.entity.OrderDetail;
import com.watermelon.model.entity.OrderStatus;
import com.watermelon.model.entity.Product;
import com.watermelon.model.entity.Size;
import com.watermelon.model.enumeration.EDeliveryStatus;
import com.watermelon.model.enumeration.EOrderStatus;
import com.watermelon.repository.BrandRepository;
import com.watermelon.repository.CategoryRepository;
import com.watermelon.repository.DeliveryMethodRepository;
import com.watermelon.repository.DeliveryStatusRepository;
import com.watermelon.repository.OrderAddressRepository;
import com.watermelon.repository.OrderDetailRepository;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.OrderStatusRepository;
import com.watermelon.repository.ProductRepository;
import com.watermelon.repository.SizeRepository;
import com.watermelon.service.OrderService;
import com.watermelon.service.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImp implements OrderService {

	ProductService productService;
	OrderRepository orderRepository;
	ProductRepository productRepository;
	OrderDetailRepository orderDetailRepository;
	OrderAddressRepository orderAddressRepository;
	DeliveryMethodRepository deliveryMethodRepository;
	OrderStatusRepository orderStatusRepository;
	DeliveryStatusRepository deliveryStatusRepository;
	SizeRepository sizeRepository;
	BrandRepository brandRepository;
	CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<Order> getAllOrder() {
		return orderRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND",id));
	}

	@Transactional
	@Override
	public Order createOrder(OrderRequest orderRequest) {
		// initial order temp
		Order order = mapRequestToOrder(orderRequest);
		// save order address
		OrderAddress orderAddress = mapRequestToOrderAddress(orderRequest.address());
		OrderAddress orderAddressSaved = orderAddressRepository.save(orderAddress);

		order.setOrderAddress(orderAddressSaved);
		// save order
		Order orderSaved = orderRepository.save(order);

		saveOrderDetails(orderRequest.listOrderDetails(), orderSaved);
		return orderSaved;
	}

	@Transactional
	@Override
	public void updateOrderStatus(OrderStatus orderStatus, Long idOrder) {
		Order order = orderRepository.findById(idOrder)
				.orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND",idOrder));
		if (order.getOrderStatus().equals(EOrderStatus.CANCELLED)) {
			throw new ForbiddenException("Cannot update order status as this order has been cancelled!");
		}
		// when user cancelled this order , system will plus product quantity on order to warehouse
		if (orderStatus.getName().equals(EOrderStatus.CANCELLED)) {
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
	public void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long idOrder) {
		Order order = orderRepository.findById(idOrder)
				.orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND",idOrder));
		if (order.getOrderStatus().equals(EOrderStatus.CANCELLED)) {
			throw new ForbiddenException("Cannot update delivery status as this order has been cancelled!");
		}
		if (order.getDeliveryStatus().equals(EDeliveryStatus.DELIVERED)) {
			throw new ForbiddenException("Cannot update delivery status as this order has been delivered!");
		}
		order.setDeliveryStatus(deliveryStatus);
		orderRepository.save(order);
	}

	private OrderDetail mapRequestToOrderDetail(OrderDetailRequest request) {

		OrderDetail orderDetail = new OrderDetail();

		Product product = productRepository.findById(request.idProduct())
				.orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND",request.idProduct()));
		orderDetail.setProduct(product);
		orderDetail.setQuantity(request.quantity());
		orderDetail.setPrice(request.price());

		Brand brand = brandRepository.findById(request.brand())
				.orElseThrow(() -> new NotFoundException("BRAND_NOT_FOUND",request.brand()));
		orderDetail.setBrand(brand.getName());

		Category category = categoryRepository.findById(request.categogy())
				.orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND",request.categogy()));
		orderDetail.setCategogy(category.getName());

		Size size = sizeRepository.findById(request.size())
				.orElseThrow(() -> new NotFoundException("SIZE_NOT_FOUND",request.size()));

		orderDetail.setSize(size.getName());

		orderDetail.setTaxPercent(request.taxPercent());
		orderDetail.setDiscountAmount(request.discountAmount());

		return orderDetail;
	}

	private OrderAddress mapRequestToOrderAddress(OrderAddressRequest request) {

		OrderAddress orderAddress = new OrderAddress();
		orderAddress.setAddressLine1(request.addressLine1());
		orderAddress.setAddressLine2(request.addressLine2());
		orderAddress.setCity(request.city());
		orderAddress.setDistrict(request.district());
		orderAddress.setProvince(request.province());
		orderAddress.setCountry(request.country());
		return orderAddress;
	}

	private Order mapRequestToOrder(OrderRequest request) {
		Order order = new Order();
		order.setNote(request.note());
		order.setTax(request.tax());
		order.setDiscount(request.discount());
		order.setTotalPrice(request.totalPrice());
		order.setDiliveryFee(request.deliveryFee());
		order.setCouponCode(request.coupondCode());
		order.setRejectReason(request.rejectReason());

		OrderStatus orderStatus = orderStatusRepository.findById(request.orderStatus())
				.orElseThrow(() -> new NotFoundException("ORDER_STATUS_NOT_FOUND",request.orderStatus()));
		order.setOrderStatus(orderStatus);

		DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(request.deliveryMethod())
				.orElseThrow(() -> new NotFoundException("DELIVERY_METHOD_NOT_FOUND",request.deliveryMethod()));
		order.setDeliveryMethod(deliveryMethod);

		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(request.deliveryStatus())
				.orElseThrow(() -> new NotFoundException("DELIVERY_STATUS_NOT_FOUND",request.deliveryStatus()));
		order.setDeliveryStatus(deliveryStatus);

		return order;
	}

	private void updateQuantityProduct(OrderDetail orderDetail, OrderDetailRequest orderDetailRequest) {
		// update quantity product by size
		productService.updateProductQuantityForSize(orderDetail.getQuantity(), orderDetail.getProduct().getId(),
				orderDetailRequest.size());
	}

	private void saveOrderDetails(Set<OrderDetailRequest> listOrderDetails, Order orderSaved) {
		// save list orderDetail
		listOrderDetails.forEach(orderDetailRq -> {
			OrderDetail orderDetail = mapRequestToOrderDetail(orderDetailRq);

			updateQuantityProduct(orderDetail, orderDetailRq);

			orderDetail.setOrder(orderSaved);
			orderDetailRepository.save(orderDetail);
		});
	}
}
