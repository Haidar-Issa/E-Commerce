package com.example.test.exception;

import com.example.test.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRunTimeException(RuntimeException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null,
                "/error"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {

        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError())
                .getDefaultMessage();

        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.BAD_REQUEST,
                message,
                null,
                "/Validation-error"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(){
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong",
                null,
                "/server-error"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCartNotFound(CartNotFoundException ex) {

        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/cart-error"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFound(ProductNotFoundException ex) {

        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/product-error"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(QuantityException.class)
    public ResponseEntity<ApiResponse<String>> handleQuantityException(QuantityException ex) {

        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null,
                "/quantity-error"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleCategoryNotFound(CategoryNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/category-error"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null,
                "/user-already-exists");
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/user-not-found"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderAlreadyExists(OrderAlreadyExistsException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                null,
                "/order-already-exists"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderNotFound(OrderNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/order-not-found"
        );
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(WebhookLogNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleWebhookLogNotFound(WebhookLogNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                "/webhook-log-not-found"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
