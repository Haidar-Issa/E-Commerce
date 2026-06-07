package com.example.test.service;

import com.example.test.dto.OrderResponseDTO;
import com.example.test.enums.Status;
import org.springframework.data.domain.Page;

public interface OrderService {

    public OrderResponseDTO create(String userId);

    public OrderResponseDTO update(String orderId, Status newStatus);

    public OrderResponseDTO findById(String id);

    public Page<OrderResponseDTO> findByUserId(String userId , int page , int size );

    public OrderResponseDTO cancel(String orderId);



}
