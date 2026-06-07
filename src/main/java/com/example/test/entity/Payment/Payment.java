package com.example.test.entity.Payment;

import com.example.test.entity.Order;
import com.example.test.entity.User;
import com.example.test.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@Data
public class Payment {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String payment_intent_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus payment_status;

    @Column(unique = true, nullable = false)
    private String provider;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(unique = true, nullable = false)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentAttempt> paymentAttempt = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private boolean signatureVerified;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;


    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = new ObjectId().toHexString();
        }
        if (this.provider == null) {
            this.provider = "Stripe";
        }
    }
}
