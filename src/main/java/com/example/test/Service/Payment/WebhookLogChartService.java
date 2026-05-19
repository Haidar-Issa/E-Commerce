package com.example.test.Service.Payment;

import com.example.test.Entity.Payment.WebhookLog;
import com.example.test.Repository.Payment.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookLogChartService {

    private final WebhookLogRepository webhookLogRepository;
    private final WebhookLogAnalyticsService webhookLogService;

    //Pie Chart : Event By Categories
    public Map<String, Long> eventByCategory() {
        return webhookLogService.countByEventCategory();
    }

    //Line Chart : Daily Event for N days
    public Map<String, Long> dailyEvent(int days) {
        Map<String, Long> map = new HashMap<>();

        for (int i = days; i >= 0; i--) {
            Instant start = Instant.now().minus(days, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            Instant end = Instant.now().plus(i, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            map.put(start.toString().substring(0, 10), webhookLogService.countByDateRange(start, end));
        }
        return map;
    }

    //Area Chart: Events By hour (today)
    public Map<Integer, Long> hourlyEvent() {
        Map<Integer, Long> map = new LinkedHashMap<>();

        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);

        for (int i = 0; i < 24; i++) {
            Instant start = Instant.now().plus(i, ChronoUnit.HOURS);
            Instant end = start.plus(i, ChronoUnit.HOURS);

            long count = webhookLogService.countByDateRange(start, end);
            map.put(i, count);
        }
        return map;
    }

    //Bar Chart: Top Retries
    public List<WebhookLog> topRetries() {
        return webhookLogService.topRetries();
    }

    //Bar Chart: Top Errors
    public List<WebhookLog> topErrors() {
        return webhookLogService.topErrors();
    }


}
