package com.lanchonete.order_payment.adapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.core.model.PaymentNotification;
import com.lanchonete.order_payment.core.usecase.interfaces.OrderPaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelas operações relacionadas a pagamentos,
 * incluindo criação de pagamentos e atualização de status.
 */

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(PaymentController.BASE_URL)
public class PaymentController {
    public static final String BASE_URL = "/api/v1/payments";

    public OrderPaymentUseCase paymentUseCase;

    /**
     * Cria uma solicitação de pagamento com base nos dados do pedido.
     *
     * @param orderSnackDTO DTO contendo os detalhes do pedido, como itens e valor.
     * @return Um byte array contendo a representação visual (exemplo: QR Code) do pagamento.
     */
    @Operation(
            summary = "Solicitar pagamento",
            description = "Recebe os detalhes do pedido e gera uma solicitação de pagamento. Retorna um QR Code ou outro formato visual que represente o pagamento.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento solicitado com sucesso",
                            content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    @PostMapping
    public ResponseEntity<byte[]> requestPayment(@RequestBody OrderSnackDTO orderSnackDTO) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE)).body(paymentUseCase.requestPayment(orderSnackDTO));
    }

    /**
     * Atualiza o status de um pagamento com base na notificação recebida.
     *
     * @param notification Objeto contendo os dados da notificação de pagamento, como ID e status.
     * @throws JsonProcessingException Caso ocorra erro ao processar a notificação.
     */
    @Operation(
            summary = "Atualizar status do pagamento",
            description = "Recebe notificações do sistema de pagamentos e atualiza o status do pagamento associado no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Status do pagamento atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na notificação"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePaymentStatus(@RequestBody PaymentNotification notification) throws JsonProcessingException {
        String not = new ObjectMapper().writeValueAsString(notification);
        if (notification.data().id().isBlank()) {
            log.error("Notification {} not found", not);
            return;
        }
        log.info("New notification arrived - {}: ", not);
        paymentUseCase.updatePaymentStatus(notification);
    }
}