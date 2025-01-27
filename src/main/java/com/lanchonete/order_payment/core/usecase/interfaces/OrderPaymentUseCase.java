package com.lanchonete.order_payment.core.usecase.interfaces;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.core.model.PaymentNotification;

public interface OrderPaymentUseCase {
    byte[] requestPayment(OrderSnackDTO orderSnackDTO);
    void updatePaymentStatus(PaymentNotification notification);

}
