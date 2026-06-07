package com.example.test.service.Payment;

import com.example.test.entity.Payment.WebhookLog;
import com.example.test.exception.WebhookLogNotFoundException;
import com.example.test.repository.Payment.WebhookLogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookLogService {
    private final WebhookLogRepository webhookLogRepository;

    public  Page<WebhookLog> findAllLogs(Pageable pageable) {
        return webhookLogRepository.findAllLogs(pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException("WebhookLogs can not found"));
    }

    public WebhookLog findByEventId(String eventId) {
        return webhookLogRepository.findByEventId(eventId).orElseThrow(
                () -> new WebhookLogNotFoundException(STR."WebhookLog By EventId: \{eventId} not found")
        );
    }

    public Page<WebhookLog> findByEventCategory(String eventCategory, Pageable pageable) {
        return webhookLogRepository.findByEventCategory(eventCategory, pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog By EventCategory: \{eventCategory}  not found"));
    }

    public List<WebhookLog> findByRefundId(String refundId) {
        return webhookLogRepository.findByRefundId(refundId)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhoookLog By RefundId: \{refundId} not found"));
    }

    public List<WebhookLog> findByPaymentId(String paymentId) {
        return webhookLogRepository.findByPayment_Id(paymentId)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog By PaymentId: \{paymentId} not found"));
    }

    public Page<WebhookLog> findByRetryCount(int retryCount, Pageable pageable) {
        return webhookLogRepository.findByRetryCount(retryCount, pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog By RetryCount: \{retryCount} not found"));
    }

    public Page<WebhookLog> findByProcessed(boolean processed, Pageable pageable) {
        return webhookLogRepository.findByProcessed(processed, pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog By Processed is : \{processed} not found"));
    }

    public WebhookLog findTopByPaymentIdOrderByCreatedAtDesc(String paymentId) {
        return webhookLogRepository.findTopByPayment_IdOrderByCreatedAtDesc(paymentId)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR." Top WebhookLog By PaymentId: \{paymentId} not Found"));
    }

    public Page<WebhookLog> findBetweenDates(Instant start, Instant end, Pageable pageable) {
        return webhookLogRepository.findBetweenDates(start, end, pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog Between startDate: \{start} , endDate: \{end} not found"));
    }

    public Page<WebhookLog> findAllLogFromPayment(String paymentId, Pageable pageable) {
        return webhookLogRepository.findAllLogsFromPayment(paymentId, pageable)
                .orElseThrow(() -> new WebhookLogNotFoundException(STR."WebhookLog By PaymentId: \{paymentId} not found"));
    }
}
