package com.example.test.entity.Payment;

import com.example.test.enums.RefundStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "refunds")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    @Id
    private String id;

    //Stripe Refund Id
    @Column(nullable = false, unique = true)
    private String providerRefundId;

    //payment-intent-id
    private String providerPaymentId;

    //Stripe charge Id
    private String chargeId;

    @DecimalMin(value = "0.0" ,  inclusive = true)
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    private String refundType; //may be FUll or Partial

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    private String reason;

    //Stripe Failure code
    private String failureCode;
    private String failureMessage;

    //balance_transaction from Stripe
    private String balanceTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_attempt_id")
    private PaymentAttempt paymentAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webhook_log_id")
    private WebhookLog webhookLog;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = new ObjectId().toHexString();
        }

    }
}
