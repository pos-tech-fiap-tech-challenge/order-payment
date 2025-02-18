package com.lanchonete.order_payment.adapters.entities;

import com.lanchonete.order_payment.core.enums.PaymentGateway;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    private String id;
    private PaymentStatus paymentStatus;
    private PaymentGateway paymentGateway;
    private PaymentType paymentType;
    private String orderId;
}
