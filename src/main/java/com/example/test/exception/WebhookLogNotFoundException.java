package com.example.test.exception;

public class WebhookLogNotFoundException extends RuntimeException {
    public WebhookLogNotFoundException(String message) {
        super(message);
    }
}
