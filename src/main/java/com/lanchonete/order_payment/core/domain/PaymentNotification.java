package com.lanchonete.order_payment.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentNotification(
    @JsonProperty("data")
    PaymentNotificationData data)
{}