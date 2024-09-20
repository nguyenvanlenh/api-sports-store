package com.watermelon.dto.request;

import com.watermelon.model.enumeration.EOrderStatus;

public record UpdateOrderStatusRequest(EOrderStatus status, String rejectReason) {

}
