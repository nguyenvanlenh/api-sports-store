package com.watermelon.dto.request;

import java.io.Serializable;

public record ProductSizeRequest(int id, int quantity) implements Serializable {
}
