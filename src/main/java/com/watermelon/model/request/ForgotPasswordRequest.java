package com.watermelon.model.request;

public record ForgotPasswordRequest(String username, String email, String phone) {

}
