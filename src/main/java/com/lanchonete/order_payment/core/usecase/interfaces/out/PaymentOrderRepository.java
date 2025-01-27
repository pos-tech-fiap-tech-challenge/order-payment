package com.lanchonete.order_payment.core.usecase.interfaces.out;

import com.lanchonete.order_payment.core.domain.Payment;

import java.util.UUID;

public interface PaymentOrderRepository {
    void savePaymentOrder(Payment paymentEntity);
    Payment findPaymentByOrderId(UUID orderId);
}
