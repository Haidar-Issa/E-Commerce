package com.example.test.Mapper;

import com.example.test.DTO.OrderItemResponseDTO;
import com.example.test.Entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")


    @Mapping(
            target = "imageUrl",
            expression = "java(orderItem.getProduct().getImages() != null && !orderItem.getProduct().getImages().isEmpty() ? orderItem.getProduct().getImages().get(0) : null)"
    )


    @Mapping(
            target = "productPrice",
            expression = "java(orderItem.getPriceAtPurchase())"
    )

    @Mapping(
            target = "totalPrice",
            expression = "java(orderItem.getPriceAtPurchase().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())))"
    )
    OrderItemResponseDTO toDTO(OrderItem orderItem);
}
