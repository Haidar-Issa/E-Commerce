package com.example.test.entity;

import com.example.test.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {

    @Id
    private String id ;

    @ManyToOne
    @JoinColumn(name = "user_id" ,  nullable = false )
    private User user;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "order", fetch = FetchType.LAZY ,  orphanRemoval = true )
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private Status status ;

    @DecimalMin(value = "0.0",  inclusive = true)
    @Column(nullable = false )
    private BigDecimal totalPrice ;

    @Column(nullable = false )
    private int totalQuantity;

    @CreationTimestamp
    private LocalDateTime  createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;

    @PrePersist
    private void prePersist(){
        if(this.id == null){
            this.id = new ObjectId().toHexString();
        }

        if(this.status == null){
            this.status = Status.PENDING;
        }
    }
}
