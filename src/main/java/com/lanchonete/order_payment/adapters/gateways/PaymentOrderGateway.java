package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.entities.PaymentEntity;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.usecase.interfaces.PaymentOrderPersistence;
import com.lanchonete.order_payment.infrastructure.persistence.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentOrderGateway implements PaymentOrderPersistence {
    private final PaymentRepository paymentRepository;

    @Override
    public void savePaymentOrder(OrderSnackDTO orderSnackDTO, UUID externalReference) {
        if(orderSnackDTO == null){
            return;
        }
        PaymentEntity paymentEntity = buildPaymentEntity(orderSnackDTO, externalReference);

        paymentRepository.save(paymentEntity);
    }

    private PaymentEntity buildPaymentEntity(OrderSnackDTO orderSnackRequest, UUID externalReference){
        return PaymentEntity
                .builder()
                .paymentGateway(orderSnackRequest.getPaymentGateway())
                .paymentStatus(PaymentStatus.OPPENED)
                .externalId(externalReference.toString())
                .orderIdSnackId(orderSnackRequest.getOrderSnackId())
                .build();
    }
}
