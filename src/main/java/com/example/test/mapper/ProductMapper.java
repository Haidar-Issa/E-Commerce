package com.example.test.mapper;

import com.example.test.dto.ProductRequestDTO;
import com.example.test.dto.ProductResponseDTO;
import com.example.test.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    @Mapping(source = "category", target = "categoryDetails")
    ProductResponseDTO toDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductRequestDTO productRequestDTO);

    List<ProductResponseDTO> toDTOList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductRequestDTO productRequestDTO, @MappingTarget Product entity);
}
