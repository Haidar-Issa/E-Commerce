package com.example.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {
    @Id
    private String id ;

    @Column(unique = true , nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if(id == null) {
            this.id = new ObjectId().toHexString();
        }
    }
}
