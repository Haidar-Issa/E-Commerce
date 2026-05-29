package com.example.test.Service;

import com.example.test.DTO.ProductRequestDTO;
import com.example.test.DTO.ProductResponseDTO;
import com.example.test.Entity.Category;
import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Mapper.ProductMapper;
import com.example.test.Repository.CategoryRepository;
import com.example.test.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    public ProductResponseDTO create(ProductRequestDTO productDto) {

        Product saved = productMapper.toEntity(productDto);
        Product entity = productRepository.save(saved);

        return productMapper.toDTO(entity);
    }

    public ProductResponseDTO update(String id, ProductRequestDTO productDto) {

        Product exists = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("product not found: " + id));

        productMapper.updateEntityFromDTO(productDto, exists);
        Product updated = productRepository.save(exists);

        return productMapper.toDTO(updated);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    public ProductResponseDTO findById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(STR."Product not found: \{id}"));
        return productMapper.toDTO(product);
    }

    public List<ProductResponseDTO> getAll(Pageable pageable) {
        List<Product> products = productRepository.findAll(pageable).getContent();
        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<ProductResponseDTO> addProducts(List<ProductRequestDTO> productsRequestDTOs) {
        List<Product> entities = productsRequestDTOs
                .stream()
                .map(productMapper::toEntity)
                .toList();
        entities.forEach(product -> {
            if (productRepository.existsByName(product.getName())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        STR."product name already exists {}\{product.getId()}"
                );
            }
        });

        List<Product> products = productRepository.saveAll(entities);


        return products.stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    public Page<ProductResponseDTO> findByCategoryName(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryName(name, pageable);

        return products.map(productMapper::toDTO);

    }

    public Page<ProductResponseDTO> findByPriceAndCategoryName(
            BigDecimal min_price,
            BigDecimal max_price,
            String categoryName,
            Pageable pageable) {

        Category category = categoryService.findByName(categoryName);
        Page<Product> products = productRepository.findByCategoryAndPriceBetween(
                category,
                min_price,
                max_price,
                pageable
        );

        return products.map(productMapper::toDTO);
    }
}
