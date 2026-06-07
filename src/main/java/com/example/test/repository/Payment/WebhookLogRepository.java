package com.example.test.repository.Payment;

import com.example.test.entity.Payment.WebhookLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebhookLogRepository extends JpaRepository<WebhookLog, String> {
    @Query("select w from WebhookLog w")
    Optional<Page<WebhookLog>> findAllLogs(Pageable pageable);

    Optional<WebhookLog> findByEventId(String eventId);

    Optional<Page<WebhookLog>> findByEventCategory(String eventCategory ,Pageable pageable);

    Optional<List<WebhookLog>> findByRefundId(String refundId);

    Optional<List<WebhookLog>> findByPayment_Id(String paymentId);

    Optional<Page<WebhookLog>> findByRetryCount(int retryCount ,Pageable pageable);

    Optional<Page<WebhookLog>> findByProcessed(boolean processed , Pageable pageable);

    Optional<WebhookLog> findTopByPayment_IdOrderByCreatedAtDesc(String paymentId);

    @Query("SELECT w FROM WebhookLog w where w.createdAt BETWEEN :start and :end")
    Optional<Page<WebhookLog>> findBetweenDates(@Param("start")Instant start,@Param("end") Instant end ,Pageable pageable);

    @Query("""
        select w from WebhookLog w where w.payment.id = :paymentId
        or w.paymentAttempt.id in (
        select pa.id from PaymentAttempt pa where pa.payment.id = :paymentId
        )
        or w.refund.id in (
        select re.id from Refund re where re.payment.id = :paymentId
        )
""")
    Optional<Page <WebhookLog>> findAllLogsFromPayment(@Param("paymentId") String paymentId ,Pageable pageable);

    //for Admin Dashboard
    Long countByEventCategory(String eventCategory);

    Long countByProcessed(boolean processed);

    Long countByRetryCountGreaterThan(Integer retryCountIsGreaterThan);

    List<WebhookLog> findByRetryCountGreaterThan(Integer retryCountIsGreaterThan);

    List<WebhookLog> findByProcessed(boolean processed);

    Long countWebhookLogByCreatedAtBetween(Instant createdAtAfter, Instant createdAtBefore);

    Long countWebhookLogByCreatedAtBetweenAndProcessed(Instant createdAtAfter, Instant createdAtBefore, boolean processed);

}
