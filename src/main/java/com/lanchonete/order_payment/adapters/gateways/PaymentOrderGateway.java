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
                .id(payment.getId())
                .paymentGateway(payment.getPaymentGateway())
                .paymentStatus(payment.getPaymentStatus())
                .orderId(String.valueOf(payment.getOrderId()))
                .build();
        paymentRepository.save(paymentSave);
    }

    @Override
    public Payment findPaymentByOrderId(UUID orderId) {
        String orderIdString = String.valueOf(orderId);
        var paymentEntity = paymentRepository.findPaymentByOrderId(orderIdString);

        return Payment.builder()
                .paymentType(paymentEntity.getPaymentType())
                .orderId(UUID.fromString(paymentEntity.getOrderId()))
                .paymentStatus(paymentEntity.getPaymentStatus())
                .paymentGateway(paymentEntity.getPaymentGateway())
                .build();
    }
}
