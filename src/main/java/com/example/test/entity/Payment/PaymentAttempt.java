package com.example.test.entity.Payment;

import com.example.test.enums.PaymentAttemptStatus;
import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment_attempt")
@Getter
@Setter
public class PaymentAttempt {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    private Integer attemptNumber;

    private String providerPaymentId; //payment-intent-id

    private String chargeId; // From Stripe

    private String clientSecret;

    private String nextAction;

    @Enumerated(EnumType.STRING)
    private PaymentAttemptStatus status;

    //Failed Details
    private String errorCode;
    private String errorMessage;
    private String declineCode;
    private String failureReason;

    //Network Details
    private String requestId;
    private String idempotencyKey;

    //Webhook Data
    private String webhookEventId;
    private String webhookType;

    // is 3D Secure
    private boolean requiresAction;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = new ObjectId().toHexString();
        }
    }

}
