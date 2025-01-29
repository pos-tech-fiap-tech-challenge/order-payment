package com.lanchonete.order_payment.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;

public record PaymentNotification(
    @JsonProperty("data")
    @Valid PaymentNotificationData data)
{}