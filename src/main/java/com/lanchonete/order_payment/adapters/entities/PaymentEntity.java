package com.lanchonete.order_payment.adapters.entities;

import com.lanchonete.order_payment.core.enums.PaymentGateway;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.enums.PaymentType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collation = "payments")
@Builder
@Data
public class PaymentEntity {
    @Id
    private String id;
    private PaymentStatus paymentStatus;
    private String externalId;
    private PaymentGateway paymentGateway;
    private PaymentType paymentType;
    private UUID orderIdSnackId;
}
