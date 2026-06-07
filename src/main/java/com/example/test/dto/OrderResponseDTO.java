package com.example.test.dto;

import com.example.test.enums.Status;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private String id;

    private Status status;

    private BigDecimal totalPrice;

    private int totalQuantity;

    private LocalDateTime createdAt;

    private List<OrderItemResponseDTO> orderItems;
}
