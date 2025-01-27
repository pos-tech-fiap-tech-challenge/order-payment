package com.lanchonete.order_payment.core.usecase.interfaces;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.model.OrderSnackPaymentStatus;

import java.util.UUID;

public interface PaymentGateway {
    QRCodeData requestQrData(OrderSnackDTO order, UUID externalReference);

    OrderSnackPaymentStatus getOrderData(String paymentId);
}
