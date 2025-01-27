package com.lanchonete.order_payment.core.usecase.interfaces.in;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.core.domain.PaymentNotification;

public interface OrderPaymentUseCase {
    byte[] requestPayment(OrderSnackDTO orderSnackDTO);
    void updatePaymentStatus(PaymentNotification notification);

}
