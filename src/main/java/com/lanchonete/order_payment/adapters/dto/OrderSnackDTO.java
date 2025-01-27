package com.lanchonete.order_payment.adapters.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanchonete.order_payment.core.enums.PaymentGateway;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSnackDTO {

    @Schema(description = "Identificador do pedido")
    @NotNull
    private UUID orderSnackId;

    @NotNull
    @Schema(description = "Gateway de pagamento. Ex: MERCADO_PAGO")
    private PaymentGateway paymentGateway;

    private List<OrderSnackItemDTO> items;

    @NotNull
    private BigDecimal totalPrice;
}
