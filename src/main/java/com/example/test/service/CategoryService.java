package com.example.test.service;


import com.example.test.dto.CategoryRequestDTO;
import com.example.test.dto.CategoryResponseDTO;
import com.example.test.entity.Category;
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
