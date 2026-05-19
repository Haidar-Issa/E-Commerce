package com.example.test.Service.Payment;

import com.example.test.Entity.Payment.WebhookLog;
import com.example.test.Repository.Payment.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookLogAnalyticsService {
    private final WebhookLogRepository webhookLogRepository;

    public Long countAll() {
        return webhookLogRepository.count();
    }

    public Map<String, Long> countByEventCategory() {

        Map<String, Long> map = new HashMap<>();
        List<String> categories = List.of("payment-intent", "charge", "refund", "payout", "dispute");

        for (String category : categories) {
            map.put(category, webhookLogRepository.countByEventCategory(category));
        }

        return map;
    }

    public Map<String, Long> countByProcessed() {
        Map<String, Long> map = new HashMap<>();
        map.put("processed", webhookLogRepository.countByProcessed(true));
        map.put("unProcessed", webhookLogRepository.countByProcessed(false));
        map.put("total", map.get("processed") + map.get("unProcessed"));

        return map;
    }

    public Long countRetries() {
        return webhookLogRepository.countByRetryCountGreaterThan(0);
    }

    public List<WebhookLog> topRetries() {
        List<WebhookLog> result = webhookLogRepository.findByRetryCountGreaterThan(0);

        return result.stream()
                .sorted(Comparator.comparing(WebhookLog::getRetryCount).reversed())
                .limit(10)
                .toList();

    }

    public List<WebhookLog> topErrors() {
        List<WebhookLog> result = webhookLogRepository.findByProcessed(false);
        return result.stream()
                .filter(w -> w.getProcessingError() != null)
                .limit(10)
                .toList();
    }

    public double avgProcessingTime() {
        List<WebhookLog> result = webhookLogRepository.findAll();

        return result.stream()
                .filter(w -> w.getProcessingTimeMs() != null)
                .mapToLong(WebhookLog::getProcessingTimeMs)
                .average()
                .orElse(0.0);
    }

    public Long countByDateRange(Instant start, Instant end) {
        return webhookLogRepository.countWebhookLogByCreatedAtBetween(start, end);
    }

    public Map<String, Long> dailyAnalytics() {

        Map<String, Long> map = new HashMap<>();
        Instant start = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant end = Instant.now();

        map.put("total", webhookLogRepository.countWebhookLogByCreatedAtBetween(start, end));
        map.put("processed", webhookLogRepository.countWebhookLogByCreatedAtBetweenAndProcessed(start, end, true));
        map.put("failed", webhookLogRepository.countWebhookLogByCreatedAtBetweenAndProcessed(start, end, false));

        return map;
    }

}
