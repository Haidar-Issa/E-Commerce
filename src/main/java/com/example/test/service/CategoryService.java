package com.example.test.Service;


import com.example.test.DTO.CategoryRequestDTO;
import com.example.test.DTO.CategoryResponseDTO;
import com.example.test.Entity.Category;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CategoryService {

     void delete(String id);

    CategoryResponseDTO update(String id, CategoryRequestDTO categoryRequestDTO);

     List<CategoryResponseDTO> getAll();

     CategoryResponseDTO getById(String id);

     Category findByName(String name);

    CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO);

    @Transactional
    List<CategoryResponseDTO> createBulk(List<CategoryRequestDTO> categoryRequestDTOS);
}
