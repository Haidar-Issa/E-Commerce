package com.example.test.Entity.Payment;

import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "webhook_logs")
@Getter
@Setter
@Builder
public class WebhookLog {
    @Id
    private String id;

//    @Column(nullable = false, unique = true)
    private String eventId;

//    @Column(nullable = false)
    private String eventName;

//    @Column(nullable = false)
    private String eventCategory; // payment_intent / charge / refund / payout / dispute

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(columnDefinition = "TEXT")
    private String rawHeaders;

    @Column(columnDefinition = "TEXT")
    private String rawQueryParams;

    private String signature;

    private Boolean signatureVerified;

    private boolean processed;

    @Column(columnDefinition = "TEXT")
    private String processingError;

    private Integer retryCount;

    @Column(columnDefinition = "TEXT")
    private String errorStackTrace;

    //Technical info
    private Long processingTimeMs;
    private String requestId;
    private String userAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_attempt_id")
    private PaymentAttempt paymentAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id")
    private Refund refund;

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
        if (this.retryCount == null) {
            this.retryCount = 0;
        }
    }
}
