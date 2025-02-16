package com.lanchonete.order_payment.core.domain;

import com.lanchonete.order_payment.core.enums.PaymentGateway;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class Payment {
    private String id;
    private PaymentStatus paymentStatus;
    private PaymentGateway paymentGateway;
    private PaymentType paymentType;
    private UUID orderId;
}
