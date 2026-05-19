package com.example.test.DTO.Payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    private BigDecimal amount;
    private String currency;
    private String description;

    private String userId;
    private String orderId;
}
