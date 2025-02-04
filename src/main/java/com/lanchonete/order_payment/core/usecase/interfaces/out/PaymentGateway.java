package com.lanchonete.order_payment.core.usecase.interfaces.out;

import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;

public interface PaymentGateway {
    QRCodeData requestQrData(OrderDTO order);

    OrderSnackPaymentStatus getOrderData(String paymentId);
}
