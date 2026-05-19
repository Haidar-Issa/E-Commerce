package com.example.test.Mapper;

import com.example.test.DTO.OrderResponseDTO;
import com.example.test.Entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "totalQuantity", target = "totalQuantity")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponseDTO toDTO(Order  order);

}
