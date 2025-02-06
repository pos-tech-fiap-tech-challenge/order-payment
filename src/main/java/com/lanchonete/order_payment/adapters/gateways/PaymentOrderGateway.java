package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.adapters.entities.PaymentEntity;
import com.lanchonete.order_payment.core.domain.Payment;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentOrderRepository;
import com.lanchonete.order_payment.adapters.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PaymentOrderGateway implements PaymentOrderRepository {
    private final PaymentRepository paymentRepository;

    @Override
    public void savePaymentOrder(Payment payment) {
        var paymentSave = PaymentEntity
                .builder()
                .paymentGateway(payment.getPaymentGateway())
                .paymentStatus(PaymentStatus.OPPENED)
                .orderId(payment.getOrderSnackId())
                .build();
        paymentRepository.save(paymentSave);
    }

    @Override
    public Payment findPaymentByOrderId(UUID orderId) {
        var paymentEntity = paymentRepository.findPaymentByOrderId(orderId);

        return Payment.builder()
                .paymentType(paymentEntity.getPaymentType())
                .orderSnackId(paymentEntity.getOrderId())
                .paymentStatus(paymentEntity.getPaymentStatus())
                .paymentGateway(paymentEntity.getPaymentGateway())
                .build();
    }
}
