package com.watermelon.dto.response;

import java.io.Serializable;

public record OrderStatusResponse (
	 Integer id,
	 String name) implements Serializable{
	

}
