package com.example.test.Controller;

import com.example.test.DTO.ApiResponse;
import com.example.test.DTO.ProductRequestDTO;
import com.example.test.DTO.ProductResponseDTO;
import com.example.test.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("category").descending());
        List<ProductResponseDTO> products = productService.getAll(pageable);

        ApiResponse<List<ProductResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Products retrieved successfully",
                products,
                "/api/products"
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> findById(@PathVariable String id) {
        ProductResponseDTO product = productService.findById(id);

        ApiResponse<ProductResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Product found successfully",
                product,
                "/api/products/" + id
        );

        return ResponseEntity.ok(response);
    }

    //    Get by Category Name
    @GetMapping("/categories/{name}")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> findByCategoryName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size , Sort.by("Category"));
        Page<ProductResponseDTO> products = productService.findByCategoryName(name, pageable);

        ApiResponse<Page<ProductResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Products extracted successfully",
                products,
                "/api/products/categories/" + name
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    GET BY USING MIN & MAX PRICE
    @GetMapping("/filter-price")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> findByPriceAndCategoryName(
            @RequestParam(required = false) BigDecimal min_price,
            @RequestParam(required = false) BigDecimal max_price,
            @RequestParam(required = false) String category_name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("price").descending());

        Page<ProductResponseDTO> products = productService.findByPriceAndCategoryName(
                min_price,
                max_price,
                category_name,
                pageable
        );

        ApiResponse<Page<ProductResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Products extracted successfully",
                products,
                "/api/products/filter_price"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> create(
            @RequestBody ProductRequestDTO productRequestDTO
    ) {
        ProductResponseDTO product = productService.create(productRequestDTO);

        ApiResponse<ProductResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "Product created successfully",
                product,
                "/api/products"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> createBulk(@RequestBody List<ProductRequestDTO> productRequestDTO) {
        List<ProductResponseDTO> products = productService.addProducts(productRequestDTO);
        ApiResponse<List<ProductResponseDTO>> response = ApiResponse.create(
                HttpStatus.CREATED,
                "Products add Successfully!",
                products,
                "api/products/bulk"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> update(
            @PathVariable String id,
            @RequestBody ProductRequestDTO productRequestDTO
    ) {
        ProductResponseDTO product = productService.update(id, productRequestDTO);

        ApiResponse<ProductResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Product updated successfully",
                product,
                "/api/products/" + id
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        productService.deleteById(id);

        ApiResponse<Void> response = ApiResponse.create(
                HttpStatus.NO_CONTENT,
                "Product deleted successfully",
                null,
                "/api/products/" + id
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
