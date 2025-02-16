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
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();

        payment = Payment.builder()
                .paymentGateway(PaymentGateway.PAGSEGURO)
                .orderId(orderId)
                .build();

        paymentEntity = PaymentEntity.builder()
                .paymentGateway(PaymentGateway.MERCADO_PAGO)
                .paymentStatus(PaymentStatus.OPPENED)
                .orderId(orderId)
                .build();
    }

    @Test
    void shouldSavePaymentOrder() {
        paymentOrderGateway.savePaymentOrder(payment);
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }

    @Test
    void shouldFindPaymentByOrderId() {
        when(paymentRepository.findPaymentByOrderId(orderId)).thenReturn(paymentEntity);

        Payment foundPayment = paymentOrderGateway.findPaymentByOrderId(orderId);

        assertNotNull(foundPayment);
        assertEquals(paymentEntity.getPaymentGateway(), foundPayment.getPaymentGateway());
        assertEquals(paymentEntity.getOrderId(), foundPayment.getOrderId());
        assertEquals(paymentEntity.getPaymentStatus(), foundPayment.getPaymentStatus());
        verify(paymentRepository, times(1)).findPaymentByOrderId(orderId);
    }
}
