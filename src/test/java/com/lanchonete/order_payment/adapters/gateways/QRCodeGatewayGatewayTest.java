package com.lanchonete.order_payment.adapters.gateways;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QRCodeGatewayGatewayTest {

    @InjectMocks
    private QRCodeGatewayGateway qrCodeGatewayGateway;

    private String text;
    private int width;
    private int height;

    @BeforeEach
    void setUp() {
        text = "https://example.com";
        width = 200;
        height = 200;
    }

    @Test
    void shouldGenerateQRCodeImage() {
        byte[] qrCodeImage = qrCodeGatewayGateway.generateQRCodeImage(text, width, height);

        assertNotNull(qrCodeImage);
        assertTrue(qrCodeImage.length > 0, "Generated QR Code image should not be empty");
    }

    @Test
    void shouldThrowRuntimeExceptionOnWriterException() {
        QRCodeGatewayGateway faultyGateway = new QRCodeGatewayGateway() {
            @Override
            public byte[] generateQRCodeImage(String text, int width, int height) {
                throw new RuntimeException("Simulated WriterException");
            }
        };

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            faultyGateway.generateQRCodeImage(text, width, height);
        });

        assertEquals("Simulated WriterException", exception.getMessage());
    }

}
