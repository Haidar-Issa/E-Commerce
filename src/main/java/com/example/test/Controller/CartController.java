package com.example.test.Controller;

import com.example.test.DTO.ApiResponse;
import com.example.test.DTO.CartResponseDTO;
import com.example.test.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final String path = "/api/cart";

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(@PathVariable String userId) {
        CartResponseDTO cart = cartService.getCartByUser(userId);
        ApiResponse<CartResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Product founded successfully",
                cart,
                path + "/" + userId
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addProductToCart(
            @PathVariable String userId,
            @PathVariable String productId) {
        CartResponseDTO cartResponseDTO = cartService.addProductToCart(userId, productId);
        ApiResponse<CartResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "Product added successfully !",
                cartResponseDTO,
                path + "/" + userId + "/add/" + productId
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateProduct(
            @RequestParam int quantity,
            @PathVariable String userId,
            @PathVariable String productId) {

        CartResponseDTO cart = cartService.updateQuantity(userId, productId, quantity);
        ApiResponse<CartResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Quantity updated successfully",
                cart,
                path + "/" + userId + "/update/" + productId
        );
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED)
                .body(response);
    }

    @DeleteMapping("/{userId}/remove/{productId}")

    public ResponseEntity<ApiResponse<CartResponseDTO>> removeProductFromCart(
            @PathVariable String userId,
            @PathVariable String productId
    ) {
        CartResponseDTO cart = cartService.removeProductFromCart(userId, productId);
        ApiResponse<CartResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Product deleted successfully",
                cart,
                path + "/" + userId + "/remove/" + productId
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<CartResponseDTO>> clearCart(@PathVariable String userId) {
        CartResponseDTO cart = cartService.clearCart(userId);
        ApiResponse<CartResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Cart cleared successfully",
                cart,
                path + "/" + userId
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
