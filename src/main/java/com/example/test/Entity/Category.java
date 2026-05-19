package com.example.test.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @Column(length = 24)
    private String id;


    @Column(nullable = false, length = 255 , unique = true  )
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT 'No description provided'")
    private String description = "No description provided";

    private String photo;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    public void onCreate() {
        if (this.id == null) {
            this.id = new ObjectId().toHexString();
        }
        this.createdDate = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
