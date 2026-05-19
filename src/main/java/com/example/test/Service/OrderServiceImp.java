package com.example.test.Service;

import com.example.test.DTO.OrderResponseDTO;
import com.example.test.Entity.*;
import com.example.test.Entity.Cart;
import com.example.test.Entity.CartItem;
import com.example.test.Entity.Order;
import com.example.test.Entity.OrderItem;
import com.example.test.Enums.Status;
import com.example.test.Exception.*;
import com.example.test.Mapper.OrderMapper;
import com.example.test.Repository.CartRepository;
import com.example.test.Repository.OrderRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderMapper orderMapper;
    private final  OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderResponseDTO create(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found by user id " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItem()) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found " + cartItem.getProduct().getId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            BigDecimal itemPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            orderItem.setTotalPrice(itemPrice);

            totalPrice = totalPrice.add(itemPrice);
            totalQuantity = totalQuantity + cartItem.getQuantity();

            orderItems.add(orderItem);

        }

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
        order.setOrderItems(orderItems);

        Order savedorder = orderRepository.save(order);

        cart.getCartItem().clear();
        cartRepository.save(cart);

        return orderMapper.toDTO(savedorder);
    }

    @Override
    public OrderResponseDTO update(String orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        Status current = order.getStatus();

        if (current.equals(newStatus)) {
            return orderMapper.toDTO(order);
        }

        if(!isTransitionalAllowed(current , newStatus)) {
            throw new RuntimeException("Invalid status transition: " + current + " → " + newStatus);
        }

        order.setStatus(newStatus);
        Order updateOrder = orderRepository.save(order);

        return orderMapper.toDTO(updateOrder);
    }


    @Override
    public OrderResponseDTO findById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id " + id)
        );
        return orderMapper.toDTO(order);
    }

    @Override
    public Page<OrderResponseDTO> findByUserId(String userId ,int page , int size ) {
        Pageable pageable = PageRequest.of(page, size , Sort.by("createdAt").descending());


        Page<Order> orders = orderRepository.findByUserId(userId , pageable).orElseThrow(
                () -> new OrderNotFoundException("order not found by userId " + userId));
        return orders.map(orderMapper::toDTO);
    }

    @Override
    public OrderResponseDTO cancel(String orderId) {
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if(order.getStatus().equals(Status.SHIPPED) || order.getStatus().equals(Status.COMPLETED)){
            throw new RuntimeException("Order cannot be cancelled at this stage");
        }

        if(order.getStatus().equals(Status.CANCELED)){
            return  orderMapper.toDTO(order);
        }
        order.setStatus(Status.CANCELED);
        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    private boolean isTransitionalAllowed(Status current , Status next){
        return switch(current) {
            case PENDING -> (next == Status.PAID ||  next == Status.CANCELED);

            case PAID ->  (next == Status.SHIPPED ||  next == Status.CANCELED);

            case SHIPPED ->   next == Status.CANCELED;

            case COMPLETED ->  false;

            case CANCELED ->  false;
        };
    }
}
