package com.example.test.Mapper.Payment;

import com.example.test.DTO.Payment.AttemptDTO;
import com.example.test.Entity.Payment.PaymentAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentAttemptMapper {
    @Mapping(source = "id", target = "attemptId")
    @Mapping(source = "providerPaymentId", target = "paymentIntentId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "nextAction", target = "nextAction")
    @Mapping(source = "clientSecret", target = "clientSecret")
    AttemptDTO toDto(PaymentAttempt attempt);
}
