package com.example.test.controller.Payment;

import com.example.test.dto.ApiResponse;
import com.example.test.dto.Payment.PaymentRequestDTO;
import com.example.test.dto.Payment.PaymentResponseDTO;
import com.example.test.dto.Payment.RefundRequestDTO;
import com.example.test.dto.Payment.RefundResponseDTO;
import com.example.test.service.Payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;
    final String path = "/api/payment";

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> create(
            @Valid @RequestBody PaymentRequestDTO paymentRequestDTO
    ) {
        PaymentResponseDTO paymentResponseDTO = paymentService.createPayment(paymentRequestDTO);
        ApiResponse<PaymentResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "Payment Intent created successfully",
                paymentResponseDTO,
                path
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RefundResponseDTO>> refund(
            @Valid @RequestBody RefundRequestDTO refundRequestDTO) {
        RefundResponseDTO refundResponseDTO = paymentService.createRefund(refundRequestDTO);
        ApiResponse<RefundResponseDTO> response = ApiResponse.create(
                HttpStatus.CREATED,
                "refund created successfully",
                refundResponseDTO,
                path + "/refund"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
