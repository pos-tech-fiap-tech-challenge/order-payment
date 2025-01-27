package com.lanchonete.order_payment.core.usecase.interfaces;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeGeneration {
    byte[] generateQRCodeImage(String text, int width, int height);
}
