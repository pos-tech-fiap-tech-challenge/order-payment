package com.lanchonete.order_payment.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public record PaymentNotification(
    @JsonProperty("data")
    PaymentNotificationData data)
{}