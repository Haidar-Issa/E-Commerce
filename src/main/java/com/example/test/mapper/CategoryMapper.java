package com.example.test.Mapper;

import com.example.test.DTO.CategoryRequestDTO;
import com.example.test.DTO.CategoryResponseDTO;
import com.example.test.Entity.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDTO toDTO(Category category);

    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    List<CategoryResponseDTO> DTO_LIST(List<Category> categoryList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(CategoryRequestDTO categoryRequestDTO,@MappingTarget Category category);

}
