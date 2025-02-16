package com.lanchonete.order_payment.core.usecase.interfaces.out;

import com.lanchonete.order_payment.core.enums.PaymentStatus;

public interface OrderGateway {
    void updateOrderStatus(PaymentStatus paymentStatus, String orderId);
}
