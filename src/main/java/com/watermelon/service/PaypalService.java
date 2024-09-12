package com.watermelon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.watermelon.dto.PaymentRequest;
import com.watermelon.dto.response.PaypalResponse;
import com.watermelon.exception.ResourceNotFoundException;
import com.watermelon.model.entity.Order;
import com.watermelon.model.enumeration.EPaymentMethod;
import com.watermelon.model.enumeration.EPaymentStatus;
import com.watermelon.repository.OrderRepository;
import com.watermelon.repository.PaymentRepository;
import com.watermelon.utils.Constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaypalService {

    APIContext apiContext;
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    
    @Value("${paypal.redirect-uri}")
	@NonFinal
	String redirectUri;

    public PaypalResponse generatePaypalLink(PaymentRequest paymentRequest, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(Constants.Paypal.CURRENCY);
        amount.setTotal(
        		String.format(Locale
        				.forLanguageTag(Constants.Paypal.CURRENCY),"%.2f",
        				paymentRequest.amount().doubleValue() * Constants.Paypal.DOLLAR_EXHANGE_RATE));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(Constants.Paypal.METHOD);

        Payment payment = new Payment();
        payment.setIntent(Constants.Paypal.INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        String approvalUrl = "";
        for (Links link : createdPayment.getLinks()) {
            if ("approval_url".equals(link.getRel())) {
                approvalUrl = link.getHref();
                break;
            }
        }

        return new PaypalResponse(approvalUrl);
    }

    public String handleSuccessPayment(
    		String paymentId, 
    		String payerId, 
    		Long orderId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);
        Order order = orderRepository.findById(orderId)
        		.orElseThrow(() -> new ResourceNotFoundException("ORDER_NOT_FOUND", orderId));
		com.watermelon.model.entity.Payment paymentClient = com.watermelon.model.entity.Payment.builder()
		.order(order)
		.amount(order.getTotalPrice())
		.paymentFee(order.getDeliveryFee())
		.paymentMethod(EPaymentMethod.PAYPAL)
		.paymentStatus(EPaymentStatus.COMPLETED)
		.build();
        
        if ("approved".equals(executedPayment.getState())) {
        	paymentRepository.save(paymentClient);
        	log.info("payment added successfully: {}", paymentClient);
            return String.format("%s?status=success&orderId=%d", redirectUri,orderId);
        }
        else 
            return  String.format("%s?status=error",redirectUri);
        
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }
}

