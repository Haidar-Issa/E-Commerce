package com.example.test.Controller;

import com.example.test.DTO.ApiResponse;
import com.example.test.DTO.OrderResponseDTO;
import com.example.test.Enums.Status;
import com.example.test.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final String path = "api/order";


    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @PathVariable String userId
    ) {
        OrderResponseDTO orderResponseDTO = orderService.create(userId);
        ApiResponse<OrderResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "Order created successfully",
                orderResponseDTO,
                path
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> findById(@PathVariable String orderId) {
        OrderResponseDTO orderResponseDTO = orderService.findById(orderId);
        ApiResponse<OrderResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Order retrieved successfully",
                orderResponseDTO,
                path + "/" + orderId
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> findByUserId(
            @PathVariable String userId,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
    ) {
        Page<OrderResponseDTO> orderResponseDTO = orderService.findByUserId(userId , page , size);
        ApiResponse<Page<OrderResponseDTO>> response = ApiResponse.create(
                HttpStatus.OK,
                "Orders retrieved successfully",
                orderResponseDTO,
                path + "/user/" + userId
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateStatus(
            @PathVariable String orderId,
            @RequestParam Status status) {

        OrderResponseDTO orderResponseDTO = orderService.update(orderId, status);
        ApiResponse<OrderResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "Order updated successfully",
                orderResponseDTO,
                path + "/" + orderId + "/status"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> cancelOrder(@PathVariable String orderId) {
        OrderResponseDTO orderResponseDTO = orderService.cancel(orderId);
        ApiResponse<OrderResponseDTO> response = ApiResponse.create(
                HttpStatus.OK,
                "order canceled successfully",
                orderResponseDTO,
                path + "/" + orderId + "/cancel"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
