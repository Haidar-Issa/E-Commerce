package com.example.test.Service.Payment;

import com.example.test.DTO.Payment.PaymentRequestDTO;
import com.example.test.DTO.Payment.PaymentResponseDTO;
import com.example.test.DTO.Payment.RefundRequestDTO;
import com.example.test.DTO.Payment.RefundResponseDTO;
import com.example.test.Entity.Order;
import com.example.test.Entity.Payment.Payment;
import com.example.test.Entity.Payment.PaymentAttempt;
import com.example.test.Entity.User;
import com.example.test.Enums.PaymentAttemptStatus;
import com.example.test.Enums.PaymentStatus;
import com.example.test.Enums.RefundStatus;
import com.example.test.Exception.OrderNotFoundException;
import com.example.test.Mapper.Payment.PaymentIntentMapper;
import com.example.test.Mapper.Payment.RefundMapper;
import com.example.test.Repository.OrderRepository;
import com.example.test.Repository.Payment.PaymentAttemptRepository;
import com.example.test.Repository.Payment.PaymentRepository;
import com.example.test.Repository.Payment.RefundRepository;
import com.example.test.Repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentIntentMapper paymentIntentMapper;
    private final RefundMapper refundMapper;
    private final RefundRepository refundRepository;

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {

        Stripe.apiKey = secretKey;

        try {
            User user = userRepository.findById(paymentRequestDTO.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Order order = orderRepository.findById(paymentRequestDTO.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            PaymentIntentCreateParams param = PaymentIntentCreateParams.builder()
                    .setAmount(order.getTotalPrice().longValue())
                    .setCurrency(paymentRequestDTO.getCurrency().toUpperCase())
                    .setDescription(STR."Order Payment: \{paymentRequestDTO.getDescription()}")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    ).putMetadata("UserId", paymentRequestDTO.getUserId())
                    .putMetadata("OrderId", paymentRequestDTO.getOrderId())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(param);


            Payment payment = Payment.builder()
                    .provider("STRIPE")
                    .amount(order.getTotalPrice())
                    .currency(paymentRequestDTO.getCurrency().toUpperCase())
                    .payment_status(PaymentStatus.PENDING)
                    .user(user)
                    .order(order)
                    .payment_intent_id(paymentIntent.getId())
                    .metadata(null)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            PaymentAttempt paymentAttempt = PaymentAttempt.builder()
                    .providerPaymentId(paymentIntent.getId())
                    .clientSecret(paymentIntent.getClientSecret())
                    .status(PaymentAttemptStatus.PENDING)
                    .nextAction(paymentIntent.getNextAction() != null ?
                            paymentIntent.getNextAction().getType() : null)
                    .payment(savedPayment)
                    .build();

            paymentAttemptRepository.save(paymentAttempt);

            savedPayment.getPaymentAttempt().add(paymentAttempt);

            return paymentIntentMapper.toDTO(savedPayment);

        } catch (StripeException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Transactional
    public RefundResponseDTO createRefund(RefundRequestDTO refundRequestDTO) {
        Stripe.apiKey = secretKey;

        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setAmount(refundRequestDTO.getAmount())
                    .setPaymentIntent(refundRequestDTO.getPaymentIntentId())
                    .setReason(RefundCreateParams.Reason.valueOf(refundRequestDTO.getReason().toUpperCase()))
                    .build();

            com.stripe.model.Refund stripeRefund = com.stripe.model.Refund.create(params);


            PaymentAttempt attempt = paymentAttemptRepository
                    .getByProviderPaymentId(stripeRefund.getPaymentIntent())
                    .orElseThrow(() -> new RuntimeException("PaymentAttempt not found"));

            Payment payment = attempt.getPayment();


            com.example.test.Entity.Payment.Refund refund = com.example.test.Entity.Payment.Refund.builder()
                    .providerRefundId(stripeRefund.getId())
                    .providerPaymentId(refundRequestDTO.getPaymentIntentId())
                    .chargeId(stripeRefund.getCharge())
                    .amount(BigDecimal.valueOf(stripeRefund.getAmount()))
                    .currency(stripeRefund.getCurrency())
                    .status(RefundStatus.valueOf(stripeRefund.getStatus().toUpperCase()))
                    .reason(stripeRefund.getReason())
                    .failureCode(stripeRefund.getFailureReason())
                    .failureMessage(null)
                    .balanceTransactionId(stripeRefund.getBalanceTransaction())
                    .payment(payment)
                    .paymentAttempt(attempt)
                    .metadata(new ObjectMapper().writeValueAsString(stripeRefund.getMetadata()))
                    .build();


            return refundMapper.toDTO(refundRepository.save(refund));

        } catch (Exception e) {
            throw new RuntimeException("Failed to create refund", e);
        }
    }
}
