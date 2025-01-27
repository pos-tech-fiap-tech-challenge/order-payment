package com.lanchonete.order_payment.core.domain;

import jakarta.validation.constraints.NotNull;

public record PaymentNotificationData(@NotNull String id){
}