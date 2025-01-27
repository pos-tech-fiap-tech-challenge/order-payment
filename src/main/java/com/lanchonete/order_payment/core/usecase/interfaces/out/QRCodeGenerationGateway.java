package com.lanchonete.order_payment.core.usecase.interfaces.out;

public interface QRCodeGenerationGateway {
    byte[] generateQRCodeImage(String text, int width, int height);
}
