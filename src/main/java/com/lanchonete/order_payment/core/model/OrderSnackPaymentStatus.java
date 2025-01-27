package com.lanchonete.order_payment.core.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderSnackPaymentStatus {
    public UUID externalOrderId;
    public String paymentStatus;
}