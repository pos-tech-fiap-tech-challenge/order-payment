package com.lanchonete.order_payment.adapters.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderSnackItemDTO(
        @Schema(description = "Valor do produto multiplicado pela quantidade solicitada")
        BigDecimal amount,
        @NotNull
        ProductDTO product,
        @Schema(description = "Quantidade solicitada do produtos")
        @Min(value = 1, message = "The quantity must be more than zero.")
        Integer quantity
) {

}
