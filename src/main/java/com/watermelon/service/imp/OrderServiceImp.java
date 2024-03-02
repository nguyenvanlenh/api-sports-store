package com.watermelon.service.imp;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.watermelon.viewandmodel.request.OrderAddressRequest;
import com.watermelon.viewandmodel.request.OrderDetailRequest;
import com.watermelon.viewandmodel.request.OrderRequest;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImp implements OrderService {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private DeliveryMethodRepository deliveryMethodRepository;

	@Autowired
	private OrderStatusRepository orderStatusRepository;

	@Autowired
	private DeliveryStatusRepository deliveryStatusRepository;

	@Autowired
	private SizeRepository sizeRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Order> getAllOrder() {
		return orderRepository.findAll();
	}

	@Override
	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found!"));
	}

	@Transactional
	@Override
	public Order saveOrder(OrderRequest orderRequest) {
		// initial order temp
		Order order = mapRequestToOrder(orderRequest);
		// save order address
		OrderAddress orderAddress = mapRequestToOrderAddress(orderRequest.address());
		OrderAddress orderAddressSaved = orderAddressRepository.save(orderAddress);

		order.setOrderAddress(orderAddressSaved);
		// save order
		Order orderSaved = orderRepository.save(order);

		saveOrderDetails(orderRequest.listOrderDetails(),orderSaved);
		return orderSaved;
	}

	@Transactional
	@Override
	public void updateOrderStatus(OrderStatus orderStatus, Long idOrder) {
		Order order = orderRepository.findById(idOrder).get();
		
		order.setOrderStatus(orderStatus);
		orderRepository.save(order);
	}

	private OrderDetail mapRequestToOrderDetail(OrderDetailRequest request) {

		OrderDetail orderDetail = new OrderDetail();

		Product product = productRepository.findById(request.idProduct())
				.orElseThrow(() -> new RuntimeException("Product not found!"));
		orderDetail.setProduct(product);
		orderDetail.setQuantity(request.quantity());
		orderDetail.setPrice(request.price());

		Brand brand = brandRepository.findById(request.brand())
				.orElseThrow(() -> new RuntimeException("Brand not found!"));
		orderDetail.setBrand(brand.getName());

		Category category = categoryRepository.findById(request.categogy())
				.orElseThrow(() -> new RuntimeException("Category not found!"));
		orderDetail.setCategogy(category.getName());

		Size size = sizeRepository.findById(request.size()).orElseThrow(() -> new RuntimeException("Size not found!"));

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
				.orElseThrow(() -> new RuntimeException("Order status not found!"));
		order.setOrderStatus(orderStatus);

		DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(request.deliveryMethod())
				.orElseThrow(() -> new RuntimeException("Delivery method not found!"));
		order.setDeliveryMethod(deliveryMethod);

		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(request.deliveryStatus())
				.orElseThrow(() -> new RuntimeException("Delivery status not found!"));
		order.setDeliveryStatus(deliveryStatus);

		return order;
	}

	private void updateQuantityProduct(OrderDetail orderDetail, OrderDetailRequest orderDetailRequest) {
		// update quantity product by size
		productService.updateQuantityProduct(orderDetail.getQuantity(), orderDetail.getProduct().getId(),
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