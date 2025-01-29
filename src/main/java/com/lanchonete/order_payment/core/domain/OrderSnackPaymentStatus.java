package com.lanchonete.order_payment.core.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class OrderSnackPaymentStatus {
    public UUID externalOrderId;
    public String paymentStatus;
}