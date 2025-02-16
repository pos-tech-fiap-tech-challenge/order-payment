package com.lanchonete.order_payment.core.usecase;

import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.domain.Payment;
import com.lanchonete.order_payment.core.domain.PaymentNotificationData;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;
import com.lanchonete.order_payment.core.domain.PaymentNotification;
import com.lanchonete.order_payment.core.usecase.interfaces.out.OrderGateway;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentGateway;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentOrderRepository;
import com.lanchonete.order_payment.core.usecase.interfaces.out.QRCodeGenerationGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderPaymentImplTest {

    @Mock
    private PaymentGateway paymentGateway;
    @Mock
    private OrderGateway orderGateway;

    @Mock
    private QRCodeGenerationGateway qrCodeGenerationGateway;

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @InjectMocks
    private OrderPaymentImpl orderPaymentImpl;

    private OrderDTO orderSnackRequest;
    private QRCodeData qrCodeData;
    private PaymentNotification paymentNotification;
    private OrderSnackPaymentStatus orderSnackPaymentStatus;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        orderSnackRequest = OrderDTO.builder()
                .orderId(orderId)
                .build();

        qrCodeData = new QRCodeData("qrCodeSampleData", "");

        paymentNotification = new PaymentNotification(
                new PaymentNotification(new PaymentNotificationData("12345")).data()
        );

        orderSnackPaymentStatus = OrderSnackPaymentStatus.builder()
                .externalOrderId(orderId)
                .paymentStatus("approved")
                .build();
    }

    @Test
    void shouldRequestPaymentAndGenerateQRCode() {
        when(paymentGateway.requestQrData(orderSnackRequest)).thenReturn(qrCodeData);
        when(qrCodeGenerationGateway.generateQRCodeImage(qrCodeData.qrData(), 250, 250)).thenReturn(new byte[]{1, 2, 3});

        byte[] qrCodeImage = orderPaymentImpl.requestPayment(orderSnackRequest);

        assertNotNull(qrCodeImage);
        assertEquals(3, qrCodeImage.length);
        verify(paymentGateway, times(1)).requestQrData(orderSnackRequest);
        verify(qrCodeGenerationGateway, times(1)).generateQRCodeImage(qrCodeData.qrData(), 250, 250);
        verify(paymentOrderRepository, times(1)).savePaymentOrder(any(Payment.class));
    }

    @Test
    void shouldUpdatePaymentStatusSuccessfully() {
        when(paymentGateway.getOrderData(paymentNotification.data().id())).thenReturn(orderSnackPaymentStatus);
        Payment payment = Payment.builder()
                .orderId(orderSnackPaymentStatus.getExternalOrderId())
                .paymentStatus(PaymentStatus.OPPENED)
                .build();
        when(paymentOrderRepository.findPaymentByOrderId(orderSnackPaymentStatus.getExternalOrderId())).thenReturn(payment);
        orderPaymentImpl.updatePaymentStatus(paymentNotification);

        verify(paymentGateway, times(1)).getOrderData(paymentNotification.data().id());
        verify(paymentOrderRepository, times(1)).findPaymentByOrderId(orderSnackPaymentStatus.getExternalOrderId());
        verify(paymentOrderRepository, times(1)).savePaymentOrder(payment);
        assertEquals(PaymentStatus.APPROVED, payment.getPaymentStatus());
    }

}
