package com.lanchonete.order_payment.core.usecase.interfaces;

import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;

import java.util.UUID;

public interface PaymentOrderPersistence {
    void savePaymentOrder(OrderSnackDTO orderSnackDTO, UUID externalReference);
}
