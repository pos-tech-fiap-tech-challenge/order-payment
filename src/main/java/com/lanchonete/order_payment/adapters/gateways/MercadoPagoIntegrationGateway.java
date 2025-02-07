package com.lanchonete.order_payment.adapters.gateways;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoItem;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrder;
import com.lanchonete.order_payment.adapters.dto.mercadopago.MercadoPagoOrderData;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MercadoPagoIntegrationGateway implements PaymentGateway {

    private String url;
    private String path;
    private String accessToken;
    private String orderDataUrl;
    private String notificationUrl;

    private static final String DEFAULT_DESCRIPTION = "Order Snack";

    private final RestTemplate restTemplate;

    public MercadoPagoIntegrationGateway(
            @Value("${integration.mercadopago.url}") String url,
            @Value("${integration.mercadopago.path}") String path,
            @Value("${integration.mercadopago.accesstoken}") String accessToken,
            @Value("${integration.mercadopago.orderDataUrl}") String orderDataUrl,
            @Value("${integration.mercadopago.notificationUrl}") String notificationUrl,
            RestTemplate restTemplate) {
        this.url = url;
        this.path = path;
        this.accessToken = accessToken;
        this.orderDataUrl = orderDataUrl;
        this.notificationUrl = notificationUrl;
        this.restTemplate = restTemplate;
    }


    @Override
    public QRCodeData requestQrData(OrderDTO order) {
        String fullUrl = url + "/" + path + "?access_token=" + accessToken;
        MercadoPagoOrder mercadoPagoOrder = convert(order);

        try {
            ResponseEntity<QRCodeData> response = restTemplate.postForEntity(fullUrl, mercadoPagoOrder, QRCodeData.class);

            return Objects.requireNonNull(response.getBody());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public MercadoPagoOrder convert(OrderDTO orderSnack) {
        MercadoPagoOrder mercadoPagoOrder = new MercadoPagoOrder();
        mercadoPagoOrder.setDescription(DEFAULT_DESCRIPTION);
        mercadoPagoOrder.setTitle(DEFAULT_DESCRIPTION);
        mercadoPagoOrder.setExternalReference(String.valueOf(orderSnack.getOrderId()));
        mercadoPagoOrder.setTotalAmount(orderSnack.getTotalPrice());
        mercadoPagoOrder.setNotificationUrl(notificationUrl);

        mercadoPagoOrder.setItems(orderSnack.getItems().stream()
                .map(item -> new MercadoPagoItem(
                        item.product().getName(),
                        item.product().getPrice(),
                        item.quantity(),
                        "unit",
                        item.product().getPrice().multiply(BigDecimal.valueOf(item.quantity()))
                ))
                .collect(Collectors.toList()));

        return mercadoPagoOrder;
    }

    @Override
    public OrderSnackPaymentStatus getOrderData(String paymentId) {
        String fullUrl = orderDataUrl + "/" + paymentId + "?access_token=" + accessToken;
        try {
            ResponseEntity<MercadoPagoOrderData> response = restTemplate.getForEntity(fullUrl, MercadoPagoOrderData.class);
            MercadoPagoOrderData mercadoPagoOrderData = Objects.requireNonNull(response.getBody());
            return new OrderSnackPaymentStatus(UUID.fromString(mercadoPagoOrderData.getExternalReference()), mercadoPagoOrderData.getStatus());

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
