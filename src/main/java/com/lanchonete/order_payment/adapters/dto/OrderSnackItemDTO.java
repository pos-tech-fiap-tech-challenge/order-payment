package com.lanchonete.order_payment.adapters.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSnackItemDTO {
    private UUID orderSnackItemId;

    private BigDecimal amount;

    private ProductDTO product;

    @Min(value = 1, message = "The quantity must be more than zero.")
    private Integer quantity;

}
