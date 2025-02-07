package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.adapters.dto.OrderSnackItemDTO;
import com.lanchonete.order_payment.adapters.dto.ProductDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrder;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrderData;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MercadoPagoIntegrationGatewayTest {

    @InjectMocks
    private MercadoPagoIntegrationGateway mercadoPagoIntegrationGateway;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mercadoPagoIntegrationGateway = new MercadoPagoIntegrationGateway(
                "https://api.mercadopago.com",
                "v1/payments",
                "test-access-token",
                "https://api.mercadopago.com/v1/orders",
                "https://webhook.site/notification", restTemplate);
    }

    @Test
    void requestQrData_ShouldReturnQRCodeData() throws Exception {
        QRCodeData mockQRCodeData = new QRCodeData("", "");
        var product = new ProductDTO();
        product.setProductId(UUID.randomUUID());
        product.setName("Bolinho");
        product.setPrice(BigDecimal.ONE);
        OrderSnackItemDTO orderSnackItemDTO = new OrderSnackItemDTO(
                BigDecimal.TEN,
                product,
                2
        );
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID());
        orderDTO.setTotalPrice(BigDecimal.valueOf(50.0));
        orderDTO.setItems(List.of(orderSnackItemDTO));

        when(restTemplate.postForEntity(anyString(), any(MercadoPagoOrder.class), eq(QRCodeData.class)))
                .thenReturn(new ResponseEntity<>(mockQRCodeData, HttpStatus.OK));

        QRCodeData qrCodeData = mercadoPagoIntegrationGateway.requestQrData(orderDTO);

        assertNotNull(qrCodeData);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(MercadoPagoOrder.class), eq(QRCodeData.class));
    }

    @Test
    void requestQrData_ShouldThrowExceptionOnError() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID());
        orderDTO.setTotalPrice(BigDecimal.valueOf(50.0));

        when(restTemplate.postForEntity(anyString(), any(MercadoPagoOrder.class), eq(QRCodeData.class)))
                .thenThrow(new RuntimeException("Erro ao conectar com MercadoPago"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                mercadoPagoIntegrationGateway.requestQrData(orderDTO));

        assertNotNull(exception.getMessage());
    }

    @Test
    void getOrderData_ShouldReturnOrderSnackPaymentStatus() throws Exception {
        String paymentId = "12345";

        MercadoPagoOrderData mockOrderData = new MercadoPagoOrderData();
        mockOrderData.setExternalReference(UUID.randomUUID().toString());
        mockOrderData.setStatus("APPROVED");

        when(restTemplate.getForEntity(anyString(), eq(MercadoPagoOrderData.class)))
                .thenReturn(new ResponseEntity<>(mockOrderData, HttpStatus.OK));

        OrderSnackPaymentStatus paymentStatus = mercadoPagoIntegrationGateway.getOrderData(paymentId);

        assertNotNull(paymentStatus);
        assertEquals("APPROVED", paymentStatus.getPaymentStatus());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(MercadoPagoOrderData.class));
    }

    @Test
    void getOrderData_ShouldThrowExceptionOnError() {
        String paymentId = "12345";

        when(restTemplate.getForEntity(anyString(), eq(MercadoPagoOrderData.class)))
                .thenThrow(new RuntimeException("Erro ao buscar dados da ordem"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                mercadoPagoIntegrationGateway.getOrderData(paymentId));

        assertNotNull(exception.getMessage());
    }
}