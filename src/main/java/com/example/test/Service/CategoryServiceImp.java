package com.example.test.Service;

import com.example.test.DTO.CategoryRequestDTO;
import com.example.test.DTO.CategoryResponseDTO;
import com.example.test.Entity.Category;
import com.example.test.Exception.CartNotFoundException;
import com.example.test.Exception.CategoryNotFoundException;
import com.example.test.Mapper.CategoryMapper;
import com.example.test.Repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO update(String id, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Category not found: " + id));
        categoryMapper.updateEntityFromDTO(categoryRequestDTO, category);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        List<Category> category = categoryRepository.findAll();

        return categoryMapper.DTO_LIST(category);
    }

    @Override
    public CategoryResponseDTO getById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Category not found: " + id));
        return categoryMapper.toDTO(category);
    }

    @Override
    public Category findByName(String name) {

        Optional<Category> category = categoryRepository.findByCategoryName(name);
        return category.orElseThrow(() -> new CategoryNotFoundException(STR."Category not found: \{name}"));
    }

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.toEntity(categoryRequestDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    @Transactional
    @Override
    public List<CategoryResponseDTO> createBulk(List<CategoryRequestDTO> categoryRequestDTOS) {
        List<Category> entities = categoryRequestDTOS.stream()
                .map(categoryMapper::toEntity)
                .toList();

        List<Category> newEntities = entities.stream().filter(
                        category -> !categoryRepository
                                .existsByName(category.getName()))
                .toList();

        if (newEntities.isEmpty()) {
            return List.of();
        }

        List<Category> savedEntities = categoryRepository.saveAll(newEntities);
        return savedEntities.stream()
                .map(categoryMapper::toDTO)
                .toList();
    }
}

