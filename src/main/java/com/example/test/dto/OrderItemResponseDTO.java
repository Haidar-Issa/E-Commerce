package com.example.test.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {

    private String id ;

    private String productId;

    private String productName;

    private BigDecimal productPrice;

    private String imageUrl;

    private int quantity;

    private BigDecimal priceAtPurchase;

    private BigDecimal totalPrice;


}
