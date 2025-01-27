package com.lanchonete.order_payment.adapters.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanchonete.order_payment.core.enums.PaymentGateway;
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

    @NotNull
    private UUID orderSnackId;

    @NotNull
    private PaymentGateway paymentGateway;

    private List<OrderSnackItemDTO> items;

    @NotNull
    private BigDecimal totalPrice;
}
