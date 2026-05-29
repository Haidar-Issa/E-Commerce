package com.example.test.DTO.Payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponseDTO {

    private String paymentIntentId;
    private String clientSecret;
    private String status;
    private String nextAction;

    private BigDecimal amount;
    private String currency;

    //System Data
    private String paymentId;
    private String paymentAttemptId;
    private String orderId;
    private String userId;

    private String provider;
    private String metadata;

}
