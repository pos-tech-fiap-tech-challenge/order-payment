package com.lanchonete.order_payment.adapters.gateways;

import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.usecase.interfaces.out.OrderGateway;
import com.lanchonete.order_payment.infra.exceptions.OrderUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderIntegrationGateway implements OrderGateway {
    private String UPDATE_ORDER_PATH;

    private RestTemplate restTemplate;

    public OrderIntegrationGateway(
            @Value("${integration.order.url}")
            String UPDATE_ORDER_PATH, RestTemplate restTemplate) {
        this.UPDATE_ORDER_PATH = UPDATE_ORDER_PATH;
        this.restTemplate = restTemplate;
    }

    @Override
    public void updateOrderStatus(PaymentStatus paymentStatus, String orderId) {
        try {
            var finalConnectionURL = UPDATE_ORDER_PATH.replace("{orderId}", orderId);
            ResponseEntity<String> response = restTemplate.postForEntity(finalConnectionURL, paymentStatus, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Status do pedido atualizado com sucesso: {}", response.getBody());
            } else {
                log.warn("Falha ao atualizar status do pedido. Código HTTP: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar status do pedido", e);
            throw new OrderUpdateException("Erro inesperado ao atualizar status do pedido", e);
        }
    }
}
