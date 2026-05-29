package com.example.test.Service.Payment;

import com.example.test.Entity.Payment.WebhookLog;
import com.example.test.Repository.Payment.WebhookLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebhookLogAnalyticsServiceWhiteBoxTest {

    @Mock
    private WebhookLogRepository webhookLogRepository;

    private WebhookLogAnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        analyticsService = new WebhookLogAnalyticsService(webhookLogRepository);
    }

    @Test
    @DisplayName("Average processing time calculation check")
    void avgProcessingTime() {
        WebhookLog log1 = new WebhookLog();
        log1.setProcessingTimeMs(100L);
        WebhookLog log2 = new WebhookLog();
        log2.setProcessingTimeMs(200L);

        when(webhookLogRepository.findAll()).thenReturn(List.of(log1, log2));

        double avgTime = analyticsService.avgProcessingTime();
        assertThat(avgTime).isEqualTo(150.0);
    }

    @Test
    @DisplayName("Checking the highest number of attempts to retrieve and remove the first 10 items")
    void topRetries() {
        List<WebhookLog> mockLogs = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            WebhookLog log = new WebhookLog();
            log.setRetryCount(i);
            mockLogs.add(log);
        }

        when(webhookLogRepository.findByRetryCountGreaterThan(0)).thenReturn(mockLogs);

        List<WebhookLog> topRetries = analyticsService.topRetries();
        assertThat(topRetries.size()).isEqualTo(10);
        assertThat(topRetries.getFirst().getRetryCount()).isEqualTo(13);
    }

    @Test
    @DisplayName("Checking daily statistics by passing real net values")
    void dailyAnalytics() {

        Map<String, Long> analytics = analyticsService.dailyAnalytics();

        assertThat(analytics).isNotNull();
        assertThat(analytics.get("processed")).isEqualTo(0L);
        assertThat(analytics.get("failed")).isEqualTo(0L);
        assertThat(analytics.get("total")).isEqualTo(0L);
    }

    @Test
    @DisplayName("Checking record classification and calculating loop categories")
    void countByEventCategory() {
        when(webhookLogRepository.countByEventCategory("payment-intent")).thenReturn(2L);
        when(webhookLogRepository.countByEventCategory("charge")).thenReturn(0L);
        when(webhookLogRepository.countByEventCategory("refund")).thenReturn(0L);
        when(webhookLogRepository.countByEventCategory("payout")).thenReturn(0L);
        when(webhookLogRepository.countByEventCategory("dispute")).thenReturn(0L);

        Map<String, Long> categoryCounts = analyticsService.countByEventCategory();

        assertThat(categoryCounts).isNotNull();
        assertThat(categoryCounts.get("payment-intent")).isEqualTo(2L);
        assertThat(categoryCounts.get("refund")).isEqualTo(0L);
    }

}
