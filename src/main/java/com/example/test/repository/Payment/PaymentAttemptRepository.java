package com.example.test.repository.Payment;

import com.example.test.entity.Payment.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, String> {

    Optional<PaymentAttempt> getByProviderPaymentId(String paymentIntentId);

}
