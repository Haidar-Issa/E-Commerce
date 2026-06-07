package com.example.test.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefundResponseDTO {

    private String refundId;
    private Long amount;
    private String currency;
    private String reason;
    private String status;
    private String paymentIntentId;
    private String chargeId;
    private String failureCode;
    private String failureMessage;
    private String balanceTransactionId;
    private Instant createAT;

}
