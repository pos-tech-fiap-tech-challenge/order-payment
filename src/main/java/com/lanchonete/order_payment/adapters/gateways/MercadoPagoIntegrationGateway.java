package com.lanchonete.order_payment.adapters.gateways;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoItem;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrder;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrderData;
import com.lanchonete.order_payment.core.model.OrderSnackPaymentStatus;
import com.lanchonete.order_payment.core.usecase.interfaces.PaymentGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MercadoPagoIntegrationGateway implements PaymentGateway {
    @Value("${integration.mercadopago.url}")
    private String url;
    @Value("${integration.mercadopago.path}")
    private String path;
    @Value("${integration.mercadopago.accesstoken}")
    private String accessToken;

    @Value("${integration.mercadopago.orderDataUrl}")
    private String orderDataUrl;
    @Value("${integration.mercadopago.notificationUrl}")
    private String notificationUrl;

    private static final String DEFAULT_DESCRIPTION = "Order Snack";

    private RestTemplate restTemplate;

    public MercadoPagoIntegrationGateway(){
        restTemplate = new RestTemplate();
    }

    @Override
    public QRCodeData requestQrData(OrderSnackDTO order, UUID externalReference) {
        String fullUrl = url + "/" + path + "?access_token=" + accessToken;
        MercadoPagoOrder mercadoPagoOrder = convert(order, externalReference);

        try {
            log.info("Requesting QRCode data to MercadoPago with order - {}", new ObjectMapper().writeValueAsString(mercadoPagoOrder));
            ResponseEntity<QRCodeData> response = restTemplate.postForEntity(fullUrl, mercadoPagoOrder, QRCodeData.class);
            log.info("Response -  {}",  new ObjectMapper().writeValueAsString(response.getBody()));

            return Objects.requireNonNull(response.getBody());
        } catch (Exception ex) {
            log.info("Error requesting QRCode data to MercadoPago - {} ",  ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public MercadoPagoOrder convert(OrderSnackDTO orderSnack, UUID externalReference) {
        MercadoPagoOrder mercadoPagoOrder = new MercadoPagoOrder();
        mercadoPagoOrder.setDescription(DEFAULT_DESCRIPTION);
        mercadoPagoOrder.setTitle(DEFAULT_DESCRIPTION);
        mercadoPagoOrder.setExternalReference(String.valueOf(externalReference));
        mercadoPagoOrder.setTotalAmount(orderSnack.getTotalPrice());
        mercadoPagoOrder.setNotificationUrl(notificationUrl);

        mercadoPagoOrder.setItems(orderSnack.getItems().stream()
                .map(item -> new MercadoPagoItem(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        "unit",
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .collect(Collectors.toList()));

        return mercadoPagoOrder;
    }

    @Override
    public OrderSnackPaymentStatus getOrderData(String paymentId) {
        String fullUrl = orderDataUrl + "/" + paymentId + "?access_token=" + accessToken;
        try {
            log.info("Requesting order data to MercadoPago with paymentId - {} ",  paymentId);
            ResponseEntity<MercadoPagoOrderData> response = restTemplate.getForEntity(fullUrl, MercadoPagoOrderData.class);
            MercadoPagoOrderData mercadoPagoOrderData = Objects.requireNonNull(response.getBody());
            log.info("Response - {} ", new ObjectMapper().writeValueAsString(mercadoPagoOrderData));
            return new OrderSnackPaymentStatus(UUID.fromString(mercadoPagoOrderData.getExternalReference()), mercadoPagoOrderData.getStatus());

        } catch (Exception ex) {
            log.error("Error requesting order data to MercadoPago - {} ", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
}
