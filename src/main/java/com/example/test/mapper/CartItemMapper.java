package com.example.test.Mapper;

import com.example.test.DTO.CartItemResponseDTO;
import com.example.test.Entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring" , uses = {ProductMapper.class})
public interface CartItemMapper {

    @Mapping(target = "totalPrice" ,  ignore = true)
    @Mapping(target = "currentPrice", ignore = true)
    @Mapping(target = "priceDifferenceMessage" , ignore = true)
    CartItemResponseDTO toDTO(CartItem cartItem);

    CartItem ToCartItem(CartItemResponseDTO cartItem);

}
