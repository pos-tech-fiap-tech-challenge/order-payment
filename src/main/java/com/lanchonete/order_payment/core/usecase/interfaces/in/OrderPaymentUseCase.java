package com.lanchonete.order_payment.core.usecase.interfaces.in;

import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.core.domain.PaymentNotification;

public interface OrderPaymentUseCase {
    byte[] requestPayment(OrderDTO orderDTO);
    void updatePaymentStatus(PaymentNotification notification);

}
