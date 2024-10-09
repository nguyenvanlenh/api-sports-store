package com.watermelon.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.watermelon.dto.request.VNPayPaymentRequest;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.dto.response.VNPayResponse;
import com.watermelon.service.VNPayService;
import com.watermelon.utils.VNPayUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/payments/vn-pay")
public class VNPayController {
	VNPayService vnPayService;

	@PostMapping
	public ResponseData<VNPayResponse> pay(
			@RequestBody VNPayPaymentRequest paymentRequest,
			HttpServletRequest request
			) throws UnsupportedEncodingException{
		String vnpIpAddr = VNPayUtils.getIpAddress(request);
        VNPayResponse data = vnPayService.generatePaymentUrl(paymentRequest,vnpIpAddr);
        log.info("VNPay payment get link success");
		return ResponseData.<VNPayResponse>builder()
				.status(HttpStatus.OK.value())
				.message("Get link payment success")
				.data(data)
				.build();
	}

	@GetMapping
	public RedirectView payCallback(
			@RequestParam(name = "vnp_ResponseCode") String code,
			@RequestParam(name = "vnp_TransactionNo") Long transactionId,
			@RequestParam(name = "vnp_OrderInfo") Long orderId) {
		String urlRedirectClient = vnPayService.processPaymentCallback(code, transactionId, orderId);
		log.info("VNPay payment created successfully");
		return new RedirectView(urlRedirectClient);

	}
	
	
}
