package com.watermelon.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.base.rest.PayPalRESTException;
import com.watermelon.dto.request.PaymentRequest;
import com.watermelon.dto.response.PaypalResponse;
import com.watermelon.dto.response.ResponseData;
import com.watermelon.service.PaypalService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequestMapping("/api/payments/paypal")
public class PaypalController {
	
	
	PaypalService paypalService;
	
	@Value("${paypal.redirect-uri}")
	@NonFinal
	String redirectUri;
	
	@PostMapping
	public ResponseData<PaypalResponse> createPaypal(
			@RequestBody PaymentRequest paymentRequest,
			HttpServletRequest request) {
		try {
            String cancelUrl = applicationUrl(request) + "/api/payments/paypal/cancel?orderId=" + paymentRequest.orderId();
            String successUrl = applicationUrl(request) + "/api/payments/paypal/success?orderId=" + paymentRequest.orderId();

            PaypalResponse response = paypalService.generatePaypalLink(paymentRequest, cancelUrl, successUrl);
            return ResponseData
    				.<PaypalResponse>builder()
    				.status(HttpStatus.OK.value())
    				.message("Paypal payment created successfully")
    				.data(response)
    				.build();
        } catch (PayPalRESTException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseData.<PaypalResponse>builder()
    				.status(HttpStatus.BAD_REQUEST.value())
    				.message("Payment creation failed")
    				.build();
        }
	}
	@GetMapping("/success")
	public RedirectView paymentSuccess(
	        @RequestParam("paymentId") String paymentId, 
	        @RequestParam("PayerID") String payerId,
	        @RequestParam("orderId") Long orderId) {
	    try {
	        String url = paypalService.handleSuccessPayment(paymentId, payerId, orderId);
	        return new RedirectView(url);
	    } catch (PayPalRESTException e) {
	        log.error("Error: ", e);
	        return new RedirectView(String.format("%s?status=error&orderId=%d", redirectUri,orderId));
	    }
	}
	@GetMapping("/error")
	public RedirectView paymentError() {
		return new RedirectView(String.format("%s?status=error", redirectUri));
	}
	@GetMapping("/cancel")
	public RedirectView paymentCancel(@RequestParam("token") String token,
			@RequestParam("orderId") Long orderId) {
		return new RedirectView(String.format("%s?status=cancel&orderId=%d", redirectUri, orderId));
	}
	private String applicationUrl(HttpServletRequest request) {
//		String scheme = request.getScheme(); scheme HTTP/HTTPS
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
