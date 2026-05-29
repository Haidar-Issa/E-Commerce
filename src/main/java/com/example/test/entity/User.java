package com.example.test.Entity;

import com.example.test.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
@Builder
public class User {

    @Id
    private String id;

    @Column(nullable = false ,length = 24 )
    private String firstName;

    @Column(nullable = false ,length = 24 )
    private String lastName;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false )
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable = false )
    @Builder.Default
    private boolean isActive = true;

    private String address;

    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;

    @PrePersist
    public void prePersist(){
        if(this.id == null){
            this.id = new ObjectId().toHexString();
        }
    }
}
