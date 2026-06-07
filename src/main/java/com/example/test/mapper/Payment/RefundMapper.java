package com.example.test.mapper.Payment;

import com.example.test.dto.Payment.RefundResponseDTO;
import com.example.test.entity.Payment.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefundMapper {

    @Mapping(source = "id", target = "refundId")
    @Mapping(source = "providerPaymentId", target = "paymentIntentId")
    @Mapping(source = "chargeId", target = "chargeId")
    @Mapping(source = "balanceTransactionId", target = "balanceTransactionId")
    @Mapping(source = "failureCode", target = "failureCode")
    @Mapping(source = "failureMessage", target = "failureMessage")
    RefundResponseDTO toDTO(Refund refund);
}
