package com.watermelon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
@Component
@Getter
public class VNPayProperties {
	
	@Value("${vnp.pay-url}")
	private String vnpPayUrl;
	
	@Value("${vnp.return-endpoint}")
	private String vnpReturnEndpoint;
	
	@Value("${vnp.tmn-code}")
	private String vnpTmnCode;
	
	@Value("${vnp.secret-key}")
	private String vnpSecretKey;
	
	@Value("${vnp.api-url}")
	private String vnpApiUrl;
	
	@Value("${vnp.version}")
	private String vnpVersion;
	
	@Value("${vnp.command}")
	private String vnpCommand;
	
	@Value("${vnp.order-type}")
	private String vnpOrderType;
	
	@Value("${vnp.curr-code}")
	private String vnpCurrCode;
	
	

}
