package com.example.test.controller;

import com.example.test.dto.ApiResponse;
import com.example.test.dto.CategoryRequestDTO;
import com.example.test.dto.CategoryResponseDTO;
import com.example.test.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final String path = "/api/categories";

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> addCategory(@RequestBody CategoryRequestDTO category) {
        CategoryResponseDTO categoryResponseDTO = categoryService.create(category);

        ApiResponse<CategoryResponseDTO> apiResponse = ApiResponse.create(
                HttpStatus.CREATED,
                "Category created Successfully",
                categoryResponseDTO,
                path
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> update(
            @PathVariable String id,
            @RequestBody CategoryRequestDTO category
    ) {
        CategoryResponseDTO categoryResponseDTO = categoryService.update(id, category);

        ApiResponse<CategoryResponseDTO> apiResponse = ApiResponse.create(
                HttpStatus.OK,
                "Category updated successfully",
                categoryResponseDTO,
                path + "/" + id
        );

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> createBulk(
           @RequestBody List<CategoryRequestDTO>categoryRequestDTOS
    ){
        List<CategoryResponseDTO> categoryResponseDTOS = categoryService.createBulk(categoryRequestDTOS);
        ApiResponse<List<CategoryResponseDTO>> apiResponse = ApiResponse.create(
                HttpStatus.OK,
                "Categories created successfully",
                categoryResponseDTOS,
                path +  "/bulk"
        );
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        categoryService.delete(id);

        ApiResponse<Void> response = ApiResponse.create(
                HttpStatus.NO_CONTENT,
                "Category deleted successfully",
                null,
                path + "/" + id
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getById(@PathVariable String id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.getById(id);

        ApiResponse<CategoryResponseDTO> apiResponse = ApiResponse.create(
                HttpStatus.OK,
                "Category founded successfully",
                categoryResponseDTO,
                path + "/" + id
        );

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAll() {
        List<CategoryResponseDTO> categoryResponseDTO = categoryService.getAll();

        ApiResponse<List<CategoryResponseDTO>> apiResponse = ApiResponse.create(
                HttpStatus.OK,
                "Category founded successfully",
                categoryResponseDTO,
                path
        );

        return ResponseEntity.ok(apiResponse);
    }

}
