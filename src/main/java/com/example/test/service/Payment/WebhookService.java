package com.example.test.service.Payment;

import com.example.test.entity.Payment.Payment;
import com.example.test.entity.Payment.PaymentAttempt;
import com.example.test.entity.Payment.WebhookLog;
import com.example.test.enums.PaymentAttemptStatus;
import com.example.test.enums.PaymentStatus;
import com.example.test.enums.RefundStatus;
import com.example.test.repository.Payment.PaymentAttemptRepository;
import com.example.test.repository.Payment.PaymentRepository;
import com.example.test.repository.Payment.RefundRepository;
import com.example.test.repository.Payment.WebhookLogRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final WebhookLogRepository webhookLogRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    @Value("${stripe.webhook-key}")
    private String webhookSecret;

    public void webhook(
            String signatureHeader,
            HttpServletRequest request
    ) throws IOException {

        String payload = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        Event event;
        long startTime = System.currentTimeMillis();

        try {
            event = Webhook.constructEvent(
                    payload,
                    signatureHeader,
                    webhookSecret
            );
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid Stripe signature", e);
        }

        String eventId = event.getId();
        String eventType = event.getType();

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        //for checked if deserializer run true and extract data from event successfully
        StripeObject stripeObject = deserializer.getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize Stripe event object"));

        Optional<WebhookLog> existingLog = webhookLogRepository.findByEventId(eventId);

        //not duplicate webhook
        if (existingLog.isPresent()) {
            return;
        }

        WebhookLog webhookLog = WebhookLog.builder()
                .eventId(eventId)
                .eventName(eventType)
                .payload(payload)
                .signature(signatureHeader)
                .processed(false)
                .retryCount(0)
                .metadata(null)
                .build();

        webhookLog.setEventCategory(eventType.split("\\.")[0]);
        webhookLog.setSignatureVerified(true);
        webhookLog.setRequestId(request.getRemoteAddr());
        webhookLog.setUserAgent(request.getHeader("User-Agent"));
        webhookLog.setRawHeaders(new ObjectMapper().writeValueAsString(request.getHeaderNames()));
        webhookLog.setProcessingTimeMs(System.currentTimeMillis() - startTime);


        webhookLog = webhookLogRepository.save(webhookLog);

        //wire between payment and paymentAttempt
        Payment payment = null;

        String paymentIntentId = extractPaymentIntentId(stripeObject);

        PaymentAttempt attempt = paymentAttemptRepository
                .getByProviderPaymentId(paymentIntentId)
                .orElse(null);

        if (attempt != null) {
            payment = attempt.getPayment();
            webhookLog.setPayment(payment);
            webhookLog.setPaymentAttempt(attempt);
            webhookLogRepository.save(webhookLog);
        }


        try {
            if (attempt != null && payment != null) {
                processEventLogic(eventType, stripeObject, attempt, payment, webhookLog);
            }
        } catch (
                Exception ex) {
            webhookLog.setProcessed(false);
            webhookLog.setProcessingError(ex.getMessage());
            webhookLogRepository.save(webhookLog);

            throw new RuntimeException("Error processing Stripe webhook", ex);
        }


    }

    private String extractPaymentIntentId(StripeObject stripeObject) {
        if (stripeObject instanceof PaymentIntent pi) return pi.getId();
        if (stripeObject instanceof Charge charge) return charge.getId();
        if (stripeObject instanceof Refund refund) return refund.getId();
        return null;
    }

    private void processEventLogic(String eventType,
                                   StripeObject stripeObject,
                                   PaymentAttempt paymentAttempt, Payment payment,
                                   WebhookLog webhookLog) {


        switch (eventType) {
            case "payment_intent.succeeded" -> handlePaymentSuccess(paymentAttempt, payment);
            case "payment_intent.failed" -> handlePaymentFailed(stripeObject, paymentAttempt, payment);
            case "payment_intent.processing" -> handlePaymentProcessing(paymentAttempt, payment);
            case "payment_intent.requires_action" -> handlePaymentRequiresAction(stripeObject, paymentAttempt, payment);
            case "charge.refunded" -> handleRefund(stripeObject, paymentAttempt, payment, webhookLog);

        }

    }

    private void handlePaymentSuccess(PaymentAttempt paymentAttempt, Payment payment) {
        paymentAttempt.setStatus(PaymentAttemptStatus.SUCCEEDED);
        payment.setPayment_status(PaymentStatus.SUCCEEDED);
        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);
    }

    private void handlePaymentFailed(StripeObject stripeObject, PaymentAttempt paymentAttempt, Payment payment) {
        PaymentIntent pi = (PaymentIntent) stripeObject;

        paymentAttempt.setStatus(PaymentAttemptStatus.FAILED);
        payment.setPayment_status(PaymentStatus.FAILED);

        paymentAttempt.setNextAction(null);

        if (pi.getLastPaymentError() != null) {
            paymentAttempt.setNextAction(pi.getLastPaymentError().getMessage());
        }

        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);

    }

    private void handlePaymentProcessing(PaymentAttempt paymentAttempt, Payment payment) {

        paymentAttempt.setStatus(PaymentAttemptStatus.PROCESSING);
        payment.setPayment_status(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);
        paymentAttemptRepository.save(paymentAttempt);
    }

    private void handlePaymentRequiresAction(StripeObject stripeObject, PaymentAttempt paymentAttempt, Payment payment) {

        PaymentIntent pi = (PaymentIntent) stripeObject;

        paymentAttempt.setStatus(PaymentAttemptStatus.REQUIRES_ACTION);
        payment.setPayment_status(PaymentStatus.REQUIRES_ACTION);

        if (pi.getNextAction().getType() != null) {
            paymentAttempt.setNextAction(pi.getNextAction().getType());
        }
        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);

    }

    private void handleRefund(StripeObject stripeObject, PaymentAttempt paymentAttempt, Payment payment, WebhookLog webhookLog) {
        Charge charge = (Charge) stripeObject;

        RefundCollection refunds = charge.getRefunds();

        if (refunds == null || refunds.getData().isEmpty()) {
            return;
        }

        Refund refund = refunds.getData().getFirst();

        String refundId = refund.getId();
        String refundStatus = refund.getStatus();

        com.example.test.entity.Payment.Refund refundEntity = refundRepository.findById(refundId).orElseGet(
                () -> createNewRefundEntity(paymentAttempt, payment, refund, charge)
        );

        refundEntity.setWebhookLog(webhookLog);
        refundRepository.save(refundEntity);

        if (refundEntity.getStatus() == RefundStatus.SUCCEEDED) {
            return;
        }

        handleRefundStatus(refund, paymentAttempt, payment, charge);

        webhookLog.setProcessed(true);

        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("refundId", refundId);
        metaMap.put("refundStatus", refundStatus);
        metaMap.put("paymentIntentId", refund.getPaymentIntent());
        metaMap.put("chargeId", charge.getId());
        metaMap.put("refundAmount", refund.getAmount());
        webhookLog.setMetadata(new ObjectMapper().writeValueAsString(metaMap));

        webhookLogRepository.save(webhookLog);

    }

    private com.example.test.entity.Payment.Refund createNewRefundEntity(PaymentAttempt paymentAttempt, Payment payment, Refund refund, Charge charge) {

        String refundId = refund.getId();
        Long refundedAmount = refund.getAmount();
        String refundStatus = refund.getStatus();
        String rPaymentIntentId = charge.getPaymentIntent();

        try {
            return com.example.test.entity.Payment.Refund.builder()
                    .providerRefundId(refundId)
                    .providerPaymentId(rPaymentIntentId)
                    .chargeId(charge.getId())
                    .amount(BigDecimal.valueOf(refundedAmount))
                    .currency(refund.getCurrency())
                    .status(RefundStatus.fromStripe(refundStatus))
                    .reason(refund.getReason())
                    .failureCode(refund.getFailureReason())
                    .failureMessage(null)
                    .balanceTransactionId(refund.getBalanceTransaction())
                    .payment(payment)
                    .paymentAttempt(paymentAttempt)
                    .webhookLog(null)
                    .metadata(new ObjectMapper().writeValueAsString(refund.getMetadata()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping metadata", e);
        }
    }

    private void handleRefundStatus(Refund refund, PaymentAttempt attempt, Payment payment, Charge charge) {
        String refundStatus = refund.getStatus();

        switch (refundStatus) {
            case "succeeded" -> handleRefundSuccess(attempt, payment, charge);
            case "failed" -> handleRefundFailed(attempt, payment);
            case "pending" -> handleRefundPending(attempt, payment);

        }
    }

    private void handleRefundSuccess(PaymentAttempt paymentAttempt, Payment payment, Charge charge) {

        Long totalAmountRefundSoFar = charge.getAmountRefunded();
        Long totalOriginalAmount = charge.getAmount();

        if (totalAmountRefundSoFar < totalOriginalAmount) {
            payment.setPayment_status(PaymentStatus.PARTIALLY_REFUNDED);
            paymentAttempt.setStatus(PaymentAttemptStatus.PARTIALLY_REFUNDED);
        } else {
            paymentAttempt.setStatus(PaymentAttemptStatus.REFUNDED);
            payment.setPayment_status(PaymentStatus.REFUNDED);
        }
        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);
    }

    private void handleRefundFailed(PaymentAttempt paymentAttempt, Payment payment) {
        paymentAttempt.setStatus(PaymentAttemptStatus.REFUND_FAILED);
        payment.setPayment_status(PaymentStatus.REFUND_FAILED);
        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);

    }

    private void handleRefundPending(PaymentAttempt paymentAttempt, Payment payment) {
        paymentAttempt.setStatus(PaymentAttemptStatus.REFUND_PENDING);
        payment.setPayment_status(PaymentStatus.REFUND_PENDING);
        paymentAttemptRepository.save(paymentAttempt);
        paymentRepository.save(payment);
    }
}

