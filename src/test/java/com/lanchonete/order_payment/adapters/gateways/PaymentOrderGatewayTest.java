package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.core.enums.PaymentGateway;
import org.junit.jupiter.api.Test;

import com.lanchonete.order_payment.adapters.entities.PaymentEntity;
import com.lanchonete.order_payment.adapters.repositories.PaymentRepository;
import com.lanchonete.order_payment.core.domain.Payment;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PaymentOrderGatewayTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentOrderGateway paymentOrderGateway;

    private Payment payment;
    private PaymentEntity paymentEntity;
    private UUID orderSnackId;

    @BeforeEach
    void setUp() {
        orderSnackId = UUID.randomUUID();

        payment = Payment.builder()
                .paymentGateway(PaymentGateway.PAGSEGURO)
                .orderSnackId(orderSnackId)
                .build();

        paymentEntity = PaymentEntity.builder()
                .paymentGateway(PaymentGateway.MERCADO_PAGO)
                .paymentStatus(PaymentStatus.OPPENED)
                .orderId(orderSnackId)
                .build();
    }

    @Test
    void shouldSavePaymentOrder() {
        paymentOrderGateway.savePaymentOrder(payment);
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void shouldFindPaymentByOrderId() {
        when(paymentRepository.findPaymentByOrderId(orderSnackId)).thenReturn(paymentEntity);

        Payment foundPayment = paymentOrderGateway.findPaymentByOrderId(orderSnackId);

        assertNotNull(foundPayment);
        assertEquals(paymentEntity.getPaymentGateway(), foundPayment.getPaymentGateway());
        assertEquals(paymentEntity.getOrderId(), foundPayment.getOrderSnackId());
        assertEquals(paymentEntity.getPaymentStatus(), foundPayment.getPaymentStatus());
        verify(paymentRepository, times(1)).findPaymentByOrderId(orderSnackId);
    }
}
