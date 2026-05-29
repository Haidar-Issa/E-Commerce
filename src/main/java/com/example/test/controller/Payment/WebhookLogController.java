package com.example.test.Controller.Payment;

import com.example.test.DTO.ApiResponse;
import com.example.test.Entity.Payment.WebhookLog;
import com.example.test.Service.Payment.WebhookLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/Webhook-log")
public class WebhookLogController {
    private final WebhookLogService webhookLogService;
    final String path = "api/Webhook-log";

    @GetMapping
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        Page<WebhookLog> webhookLogs = webhookLogService.findAllLogs(pageable);
        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "WebhookLogs retrieved successfully",
                webhookLogs,
                path
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-event")
    public ResponseEntity<ApiResponse<WebhookLog>> getLogByEventId(@RequestParam String eventId) {
        ApiResponse<WebhookLog> response = ApiResponse.create(
                HttpStatus.OK,
                "WebhookLog retrieved successfully",
                webhookLogService.findByEventId(eventId),
                path + eventId
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogsByEventCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createAt").descending());
        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "Webhooks retrieved successfully",
                webhookLogService.findByEventCategory(category, pageable),
                STR."/\{path}/by-category/\{category}"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-refund")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> getAllLogsByRefund(@RequestParam String refundId) {
        ApiResponse<List<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully",
                webhookLogService.findByRefundId(refundId),
                STR."/\{path}/by-refund"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("by-payment")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> getAllLogsByPayment(@RequestParam String paymentId) {
        ApiResponse<List<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully",
                webhookLogService.findByPaymentId(paymentId),
                STR."/\{path}/by-bayment"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/retries")
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogsByRetries(
            @RequestParam int retryCount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully",
                webhookLogService.findByRetryCount(retryCount, pageable),
                STR."\{path}/retries"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/processed")
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogsByProcessed(
            @RequestParam boolean processed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully",
                webhookLogService.findByProcessed(processed, pageable),
                STR."\{path}/processed"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogsByDateRange(
            @RequestParam Instant start,
            @RequestParam Instant end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully BETWEEN START AND END DATES",
                webhookLogService.findBetweenDates(start, end, pageable),
                STR."\{path}/date-range"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/payment-id")
    public ResponseEntity<ApiResponse<Page<WebhookLog>>> getAllLogsByPaymentId(@RequestParam String paymentId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);

        ApiResponse<Page<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "webhooks retrieved successfully",
                webhookLogService.findAllLogFromPayment(paymentId, pageable),
                STR."\{path}/payment-id"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
