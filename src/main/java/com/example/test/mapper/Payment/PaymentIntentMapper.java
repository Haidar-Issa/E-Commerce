package com.example.test.mapper.Payment;

import com.example.test.dto.Payment.AttemptDTO;
import com.example.test.dto.Payment.PaymentResponseDTO;
import com.example.test.entity.Payment.Payment;
import com.example.test.entity.Payment.PaymentAttempt;
import com.example.test.mapper.OrderMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;


@Mapper(
        componentModel = "spring",
        uses = {PaymentAttemptMapper.class,
        PaymentIntentMapper.class,
        OrderMapper.class,}
)
public abstract class PaymentIntentMapper {

    @Autowired
     protected PaymentAttemptMapper attemptMapper;

    @Mapping(source = "id" , target = "paymentId")
    @Mapping(target = "clientSecret" , ignore = true )
    @Mapping(target = "status" , ignore = true)
    @Mapping(target = "nextAction" , ignore = true)
    @Mapping(target = "paymentIntentId" , ignore = true)
    @Mapping(source = "amount" , target = "amount")
    @Mapping(source = "order.id" , target = "orderId")
    @Mapping(source = "user.id" , target = "userId")
    @Mapping(target = "provider" , constant = "STRIPE")
    @Mapping(source = "metadata" , target = "metadata")
    @Mapping(source = "currency" , target = "currency")
    public abstract PaymentResponseDTO toDTO(Payment payment);

    @AfterMapping
    protected void enrich(
            Payment payment,
            @MappingTarget PaymentResponseDTO.PaymentResponseDTOBuilder dto
    ){
        PaymentAttempt lastPaymentAttempt = payment.getPaymentAttempt()
                .stream()
                .max(Comparator.comparing(PaymentAttempt :: getCreatedAt))
                .orElse(null);

        if(lastPaymentAttempt == null){
            return;
        }

        AttemptDTO attemptDTO = attemptMapper.toDto(lastPaymentAttempt);

        dto.paymentIntentId(attemptDTO.getPaymentIntentId());
        dto.clientSecret(attemptDTO.getClientSecret());
        dto.status(attemptDTO.getStatus());
        dto.nextAction(attemptDTO.getNextAction());
        dto.paymentAttemptId(attemptDTO.getAttemptId());

    }

}
