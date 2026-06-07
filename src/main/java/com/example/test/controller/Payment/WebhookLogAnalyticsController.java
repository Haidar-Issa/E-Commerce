package com.example.test.controller.Payment;

import com.example.test.dto.ApiResponse;
import com.example.test.entity.Payment.WebhookLog;
import com.example.test.service.Payment.WebhookLogAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook-log/analytics")
@RequiredArgsConstructor
public class WebhookLogAnalyticsController {

    private final WebhookLogAnalyticsService webhookLogAnalyticsService;
    final String path = "/api/webhook-log/analytics";

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countAll() {
        ApiResponse<Long> response = ApiResponse.create(
                HttpStatus.OK,
                "WebhookLog count retrieved successfully",
                webhookLogAnalyticsService.countAll(),
                STR."\{path}/count"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/count/by-category")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countByCategory() {
        ApiResponse<Map<String, Long>> response = ApiResponse.create(
                HttpStatus.OK,
                "WebhookLog count by category retrieved successfully",
                webhookLogAnalyticsService.countByEventCategory(),
                STR."\{path}/count/by-category"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/count/processed")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countProcessed() {
        ApiResponse<Map<String, Long>> response = ApiResponse.create(
                HttpStatus.OK,
                "WebhookLog count By Processed retrieved successfully",
                webhookLogAnalyticsService.countByProcessed(),
                STR."\{path}/processed"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/count/retries")
    public ResponseEntity<ApiResponse<Long>> countRetries() {
        ApiResponse<Long> response = ApiResponse.create(
                HttpStatus.OK,
                "Count-Retries for WebhookLog retrieved successfully",
                webhookLogAnalyticsService.countRetries(),
                STR."\{path}/retries"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/top-retries")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> countTopRetries() {
        ApiResponse<List<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "Top-Retries for WebhookLog retrieved successfully",
                webhookLogAnalyticsService.topRetries(),
                STR."\{path}/top-retries"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/top-errors")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> countTopError() {
        ApiResponse<List<WebhookLog>> response = ApiResponse.create(
                HttpStatus.OK,
                "Top-Error in WebhookLog retrieved successfully",
                webhookLogAnalyticsService.topErrors(),
                STR."\{path}/top-errors"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/avg-processing-time")
    public ResponseEntity<ApiResponse<Double>> avgProcessingTime() {
        ApiResponse<Double> response = ApiResponse.create(
                HttpStatus.OK,
                "avg-processing-time for Webhook retrieved successfully",
                webhookLogAnalyticsService.avgProcessingTime(),
                STR."\{path}avg-processing-time"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/count/by-date-range")
    public ResponseEntity<ApiResponse<Long>> countByDateRange(
            @RequestParam Instant start,
            @RequestParam Instant end
    ) {
        ApiResponse<Long> response = ApiResponse.create(
                HttpStatus.OK,
                "Count WebhookLog by date-range retrieved successfully",
                webhookLogAnalyticsService.countByDateRange(start, end),
                STR."\{path}/count/by-date-range"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<Map<String, Long>>> daily() {
        ApiResponse<Map<String, Long>> response = ApiResponse.create(
                HttpStatus.OK,
                "Daily WebhookLog retieved successfully",
                webhookLogAnalyticsService.dailyAnalytics(),
                STR."\{path}/daily"
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
