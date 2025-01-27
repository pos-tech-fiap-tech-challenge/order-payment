package com.lanchonete.order_payment.adapters.dto.mercadopago;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoOrder {
    private String description;
    @JsonProperty("external_reference")
    private String externalReference;
    private List<MercadoPagoItem> items;
    private String title;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
    @JsonProperty("notification_url")
    private String notificationUrl;
}
