package com.lanchonete.order_payment.core.domain;

import jakarta.validation.constraints.NotBlank;

public record PaymentNotificationData(@NotBlank String id){
}