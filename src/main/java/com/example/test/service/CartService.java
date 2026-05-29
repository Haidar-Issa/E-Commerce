package com.example.test.Service;

import com.example.test.DTO.CartItemResponseDTO;
import com.example.test.DTO.CartResponseDTO;
import com.example.test.Entity.Cart;
import com.example.test.Entity.CartItem;
import com.example.test.Entity.Product;
import com.example.test.Exception.CartNotFoundException;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Mapper.CartItemMapper;
import com.example.test.Repository.CartItemRepository;
import com.example.test.Repository.CartRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Convert CartItem → DTO with price logic
    private CartItemResponseDTO buildCartItemDTO(CartItem item) {

        CartItemResponseDTO dto = cartItemMapper.toDTO(item);

        BigDecimal currentPrice = item.getProduct().getPrice();
        dto.setCurrentPrice(currentPrice);

        BigDecimal totalPrice = currentPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        dto.setTotalPrice(totalPrice);

        BigDecimal oldPrice = item.getPriceAtAddition();
        BigDecimal difference = currentPrice.subtract(oldPrice);

        if (difference.compareTo(BigDecimal.ZERO) < 0) {
            dto.setPriceDifferenceMessage("Price dropped by " + difference.abs());
        } else if (difference.compareTo(BigDecimal.ZERO) > 0) {
            dto.setPriceDifferenceMessage("Price increased by " + difference);
        } else {
            dto.setPriceDifferenceMessage(null);
        }

        return dto;
    }

    // Get Cart
    public CartResponseDTO getCartByUser(String userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found")));
                    return cartRepository.save(newCart);
                });

        List<CartItemResponseDTO> items = new ArrayList<>();
        BigDecimal totalCartPrice = BigDecimal.ZERO;

        for (CartItem item : cart.getCartItem()) {

            CartItemResponseDTO dto = buildCartItemDTO(item);
            items.add(dto);

            totalCartPrice = totalCartPrice.add(dto.getTotalPrice());
        }

        return new CartResponseDTO(
                cart.getId(),
                items,
                totalCartPrice
        );
    }

    // Add Product
    public CartResponseDTO addProductToCart(String userId, String productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found")));
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        CartItem cartItem = cart.getCartItem().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setPriceAtAddition(product.getPrice());
        }

        cartItemRepository.save(cartItem);

        return getCartByUser(userId);
    }

    // Remove Product
    public CartResponseDTO removeProductFromCart(String userId, String productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        CartItem cartItem = cart.getCartItem().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + productId));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }

        return getCartByUser(userId);
    }

    // Clear Cart
    /// //////
    public CartResponseDTO clearCart(String userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        cartRepository.deleteByCartId(cart.getId());

        return getCartByUser(userId);
    }

    // Update Quantity
    public CartResponseDTO updateQuantity(String userId, String productId, int newQuantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        CartItem cartItem = cart.getCartItem().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + productId));

        if (newQuantity == 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }

        return getCartByUser(userId);
    }
}

