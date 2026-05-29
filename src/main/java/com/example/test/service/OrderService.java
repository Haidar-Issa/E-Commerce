package com.example.test.Service;

import com.example.test.DTO.OrderResponseDTO;
import com.example.test.Enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public OrderResponseDTO create(String userId);

    public OrderResponseDTO update(String orderId, Status newStatus);

    public OrderResponseDTO findById(String id);

    public Page<OrderResponseDTO> findByUserId(String userId , int page , int size );

    public OrderResponseDTO cancel(String orderId);



}
