package com.lanchonete.order_payment.core.usecase.interfaces.out;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;

import java.util.UUID;

public interface PaymentGateway {
    QRCodeData requestQrData(OrderSnackDTO order);

    OrderSnackPaymentStatus getOrderData(String paymentId);
}
