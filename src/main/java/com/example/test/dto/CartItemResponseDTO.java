package com.example.test.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    private String id;

    private ProductResponseDTO product;

    private BigDecimal currentPrice;

    private BigDecimal totalPrice;

    private int quantity;

    private String priceDifferenceMessage;

}
