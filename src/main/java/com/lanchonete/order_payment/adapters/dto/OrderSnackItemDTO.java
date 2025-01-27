package com.lanchonete.order_payment.adapters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Valor do produto multiplicado pela quantidade solicitada")
    private BigDecimal amount;

    private ProductDTO product;

    @Schema(description = "Quantidade solicitada do produtos")
    @Min(value = 1, message = "The quantity must be more than zero.")
    private Integer quantity;

}
