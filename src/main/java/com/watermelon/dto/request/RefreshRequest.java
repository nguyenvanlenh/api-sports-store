package com.watermelon.dto.request;

import java.io.Serializable;

public record RefreshRequest(String token) implements Serializable {

}
