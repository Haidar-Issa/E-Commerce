package com.example.test.Repository.Payment;

import com.example.test.Entity.Payment.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {
    Optional<Refund> findByProviderPaymentId(String paymentIntentId);

    Refund getByProviderPaymentId(String providerPaymentId);
    Optional<Refund> findRefundByProviderPaymentId(String providerPaymentId);
}
