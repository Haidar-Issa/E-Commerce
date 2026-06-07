package com.example.test.controller.Payment;

import com.example.test.dto.ApiResponse;
import com.example.test.entity.Payment.WebhookLog;
import com.example.test.service.Payment.WebhookLogChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook-log/charts")
public class WebhookLogChartController {
    private final WebhookLogChartService webhookLogChartService;
    final String path = "/api/webhook-log/charts";

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse<Map<String, Long>>> eventByCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "Events by category chart data",
                        webhookLogChartService.eventByCategory(),
                        path + "/by-category"
                )
        );
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<Map<String, Long>>> eventByDaily(
            @RequestParam(defaultValue = "30") int days
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "Daily events chart data",
                        webhookLogChartService.dailyEvent(days),
                        path + "/daily"
                )
        );
    }

    @GetMapping("/hourly")
    public ResponseEntity<ApiResponse<Map<Integer, Long>>> eventByHourly() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "Hourly events chart data",
                        webhookLogChartService.hourlyEvent(),
                        path + "/hourly"
                )
        );
    }

    @GetMapping("/top-retries")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> topRetries(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.create(
                        HttpStatus.OK,
                        "Top retries chart data",
                        webhookLogChartService.topRetries(),
                        path + "/top-retries"
                )
        );
    }

    @GetMapping("/top-errors")
    public ResponseEntity<ApiResponse<List<WebhookLog>>> topErrors() {
        return ResponseEntity.ok(
                ApiResponse.create(
                        HttpStatus.OK,
                        "Top errors chart data",
                        webhookLogChartService.topErrors(),
                        path + "/top-errors"
                )
        );
    }


}
