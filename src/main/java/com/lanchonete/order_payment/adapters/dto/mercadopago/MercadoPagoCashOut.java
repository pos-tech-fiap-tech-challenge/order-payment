package com.lanchonete.order_payment.adapters.dto.mercadopago;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MercadoPagoCashOut {
    private BigDecimal amount;
}
