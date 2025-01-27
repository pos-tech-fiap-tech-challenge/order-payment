package com.lanchonete.order_payment.adapters.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QRCodeData(
        @JsonProperty("in_store_order_id")
        String inStoreData,
        @JsonProperty("qr_data")
        String qrData
) {
}
