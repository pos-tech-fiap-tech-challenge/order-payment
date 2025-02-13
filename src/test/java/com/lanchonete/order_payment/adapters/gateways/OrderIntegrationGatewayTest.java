package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.infra.exceptions.OrderUpdateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderIntegrationGatewayTest {

    @Mock
    private RestTemplate restTemplate;

    private static final String ORDER_UPDATE_URL = "https://api.exemplo.com/orders/update";

    @InjectMocks
    private OrderIntegrationGateway orderIntegrationGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderIntegrationGateway = new OrderIntegrationGateway(ORDER_UPDATE_URL, restTemplate);
    }

    @Test
    void updateOrderStatus_Success() {
        PaymentStatus paymentStatus = PaymentStatus.EXPIRED;

        when(restTemplate.postForEntity(anyString(), any(PaymentStatus.class), any()))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        assertDoesNotThrow(() -> orderIntegrationGateway.updateOrderStatus(paymentStatus));

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(PaymentStatus.class), any());
    }

    @Test
    void updateOrderStatus_Failure_ShouldLogWarning() {
        PaymentStatus paymentStatus = PaymentStatus.APPROVED;

        when(restTemplate.postForEntity(anyString(), any(PaymentStatus.class), any()))
                .thenReturn(new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST));

        assertDoesNotThrow(() -> orderIntegrationGateway.updateOrderStatus(paymentStatus));

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(PaymentStatus.class), any());
    }

    @Test
    void updateOrderStatus_Exception_ShouldThrowOrderUpdateException() {
        PaymentStatus paymentStatus = PaymentStatus.OPPENED;

        when(restTemplate.postForEntity(any(), any(PaymentStatus.class), eq(String.class)))
                .thenThrow(new RuntimeException("Erro na API"));

        Exception exception = assertThrows(OrderUpdateException.class, () ->
                orderIntegrationGateway.updateOrderStatus(paymentStatus));

        assertTrue(exception.getMessage().contains("Erro inesperado ao atualizar status do pedido"));

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(PaymentStatus.class), any());
    }
}
