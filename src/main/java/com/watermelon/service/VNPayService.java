package com.watermelon.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watermelon.config.VNPayProperties;
import com.watermelon.dto.request.VNPayPaymentRequest;
import com.watermelon.dto.response.VNPayResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.exception.VNPayPaymentException;
import com.watermelon.model.entity.Order;
import com.watermelon.model.entity.Payment;
import com.watermelon.model.enumeration.EPaymentMethod;
import com.watermelon.model.enumeration.EPaymentStatus;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.PaymentRepository;
import com.watermelon.utils.VNPayUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {
	VNPayProperties vnPayProperties;
	PaymentRepository paymentRepository;
	OrderRepository orderRepository;
	
	@Value("${vnp.redirect-uri}")
	@NonFinal
	String redirectUri;

	@Transactional
	public VNPayResponse generatePaymentUrl(VNPayPaymentRequest paymentRequest, String vnpIpAddr)
			throws UnsupportedEncodingException {

		BigDecimal realAmount = calculateAmount(paymentRequest);
		String vnpTxnRef = VNPayUtils.getRandomNumber(8);
		
		Map<String, String> vnpParams = buildVNPayParams(paymentRequest, vnpIpAddr, vnpTxnRef, realAmount);
		
		String queryUrl = buildQuery(vnpParams);
		String secureHash = VNPayUtils.hmacSHA512(vnPayProperties.getVnpSecretKey(), queryUrl);
		StringBuilder signedQueryUrl = new StringBuilder(queryUrl);
		signedQueryUrl.append("&vnp_SecureHash=").append(secureHash);

		String paymentUrl = String.format("%s?%s", vnPayProperties.getVnpPayUrl(),signedQueryUrl);

		return VNPayResponse.builder().code("200").status("success").url(paymentUrl).build();
	}

	@Transactional
	public String processPaymentCallback(String code, Long paymentId, Long orderId) {

		if ("00".equals(code)) {
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", orderId));
			Payment paymentClient = Payment.builder()
					.id(paymentId)
					.order(order)
					.amount(order.getTotalPrice())
					.paymentFee(order.getDeliveryFee())
					.paymentMethod(EPaymentMethod.VNPAY)
					.paymentStatus(EPaymentStatus.COMPLETED)
					.build();
			paymentRepository.save(paymentClient);
			return String.format("%s?status=success", redirectUri);
        }
        else {
        	return  String.format("%s?status=error&orderId=%d", redirectUri,orderId);
        }
	}

	private BigDecimal calculateAmount(VNPayPaymentRequest paymentRequest) {
		return paymentRequest.amount()
				.add(paymentRequest.paymentFee())
				.multiply(BigDecimal.valueOf(100));
	}

	private Map<String, String> buildVNPayParams(
			VNPayPaymentRequest paymentRequest, 
			String vnpIpAddr, 
			String vnpTxnRef,
			BigDecimal realAmount) {
		Map<String, String> vnpParams = new HashMap<>();
		vnpParams.put("vnp_Version", vnPayProperties.getVnpVersion());
		vnpParams.put("vnp_Command", vnPayProperties.getVnpCommand());
		vnpParams.put("vnp_TmnCode", vnPayProperties.getVnpTmnCode());
		vnpParams.put("vnp_Amount", String.valueOf(realAmount));
		vnpParams.put("vnp_CurrCode", vnPayProperties.getVnpCurrCode());
		vnpParams.put("vnp_TxnRef", vnpTxnRef);
		vnpParams.put("vnp_OrderInfo", paymentRequest.orderId().toString());
		vnpParams.put("vnp_OrderType", vnPayProperties.getVnpOrderType());
		vnpParams.put("vnp_Locale", Optional.ofNullable(paymentRequest.language()).orElse("vn"));
		vnpParams.put("vnp_ReturnUrl", vnPayProperties.getVnpReturnUrl());
		vnpParams.put("vnp_IpAddr", vnpIpAddr);
		
		if (paymentRequest.bankCode() != null && !paymentRequest.bankCode().isEmpty()) {
			vnpParams.put("vnp_BankCode", paymentRequest.bankCode());
		}
		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		vnpParams.put("vnp_CreateDate", formatter.format(cld.getTime()));
		cld.add(Calendar.MINUTE, 15);
		vnpParams.put("vnp_ExpireDate", formatter.format(cld.getTime()));
		return vnpParams;
	}

	private String buildQuery(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> fieldNames = new ArrayList<>(params.keySet());
		Collections.sort(fieldNames);
		StringBuilder query = new StringBuilder();
		fieldNames.stream().forEach(fieldName -> {
			String fieldValue = params.get(fieldName);
			if (fieldValue != null && !fieldValue.isEmpty()) {
				try {
					query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=')
							.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())).append('&');
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					log.debug(e.getMessage());
					throw new VNPayPaymentException(e.getMessage());
					
				}
			}
		});
		return query.substring(0, query.length() - 1); // Remove last '&'
	}

}
