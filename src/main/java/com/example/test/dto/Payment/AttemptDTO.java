package com.example.test.DTO.Payment;

import com.example.test.Enums.PaymentAttemptStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttemptDTO {

    private String attemptId;
    private String paymentIntentId;
    private String status;
    private String clientSecret;
    private String nextAction;

}
