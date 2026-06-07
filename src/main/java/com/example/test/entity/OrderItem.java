package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "order_items")
public class OrderItem {

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = new ObjectId().toHexString();
        }
        calculatePrice();
    }

    @PreUpdate
    private void preUpdate() {
        calculatePrice();
    }

    private void calculatePrice() {
        if (this.product != null) {
            this.priceAtPurchase = product.getPrice();
            this.totalPrice = this.priceAtPurchase.multiply(BigDecimal.valueOf(quantity));
        }
    }

}
