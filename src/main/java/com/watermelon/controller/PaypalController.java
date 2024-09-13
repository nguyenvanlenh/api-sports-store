package com.watermelon.controller;


import org.springframework.beans.factory.annotation.Value;
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
            String cancelUrl = applicationUrl(request) + "/api/payments/paypal/cancel";
            String successUrl = applicationUrl(request) + "/api/payments/paypal/success?orderId=" + paymentRequest.orderId();

            PaypalResponse response = paypalService.generatePaypalLink(paymentRequest, cancelUrl, successUrl);
            return new ResponseData<>(200, "Create payment success", response);
        } catch (PayPalRESTException e) {
            log.error("Error: {}", e.getMessage());
            return new ResponseData<>(500, "Payment creation failed", null);
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
	        return new RedirectView(String.format("%s?status=error", redirectUri));
	    }
	}
	@GetMapping("/error")
	public RedirectView paymentError() {
		return new RedirectView(String.format("%s?status=error", redirectUri));
	}
	@GetMapping("/cancel")
	public RedirectView paymentCancel(@RequestParam("token") String token) {
		return new RedirectView(String.format("%s?status=cancel", redirectUri));
	}
	private String applicationUrl(HttpServletRequest request) {
//		String scheme = request.getScheme(); scheme HTTP/HTTPS
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
