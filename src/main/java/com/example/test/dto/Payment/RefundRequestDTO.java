package com.example.test.dto.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundRequestDTO {

    private String paymentIntentId;
    private Long amount;
    private String reason; // optional: duplicate, requested_by_customer, fraudulent

}
