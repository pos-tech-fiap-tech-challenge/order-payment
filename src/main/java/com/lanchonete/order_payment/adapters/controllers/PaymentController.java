package com.lanchonete.order_payment.adapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.core.model.PaymentNotification;
import com.lanchonete.order_payment.core.usecase.interfaces.OrderPaymentUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(PaymentController.BASE_URL)
public class PaymentController {
    public static final String BASE_URL = "/payments";

    public OrderPaymentUseCase paymentUseCase;


    @PostMapping
    public ResponseEntity<byte[]> requestPayment(@RequestBody OrderSnackDTO orderSnackDTO) {
        return ResponseEntity.ok().contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE)).body(paymentUseCase.requestPayment(orderSnackDTO));
    }

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