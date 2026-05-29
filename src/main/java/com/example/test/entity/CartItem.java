package com.example.test.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cartItem")
public class CartItem {

    @Id
    private String id;

    @DecimalMin(value = "0.0" , inclusive = true)
    @Column(nullable = false)
    private BigDecimal priceAtAddition ;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Column(nullable = false)
    private Instant addedAt;

    @PrePersist
    public void prePersist()
    {
        if(id == null)
        {
            id = new ObjectId().toString();
        }
        this.addedAt = Instant.now();
    }
}
