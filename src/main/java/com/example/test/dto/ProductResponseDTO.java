package com.example.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDTO {

    private String id;

    private CategoryResponseDTO categoryDetails;

    private String description;

    private BigDecimal price;

    private List<String> images;

    private String name;

    private Integer quantity;
}
