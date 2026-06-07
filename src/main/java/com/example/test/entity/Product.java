package com.example.test.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @Column(length = 24)
    private String id ;

    @NotBlank
    @Column(nullable = false , length = 255 , unique = true)
    private String name;

    @Column(columnDefinition = "TEXT DEFAULT 'No description provided'" ,nullable = false )
    private String description ="No description provided";

    @Column(nullable = false)
    @DecimalMin(value = "0.0" ,inclusive = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Min(0)
    private Integer quantity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id" ,  nullable = false)
    private Category category;

    @Column(nullable = false , updatable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @PrePersist
    public void onCreate(){
        if(this.id==null){
            this.id = new ObjectId().toHexString();
        }
        this.createdDate = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }


    @PreUpdate
    public void onUpdate(){
        this.updateAt = LocalDateTime.now();
    }
}
