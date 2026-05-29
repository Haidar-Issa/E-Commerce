package com.example.test.Exception;

public class WebhookLogNotFoundException extends RuntimeException {
    public WebhookLogNotFoundException(String message) {
        super(message);
    }
}
