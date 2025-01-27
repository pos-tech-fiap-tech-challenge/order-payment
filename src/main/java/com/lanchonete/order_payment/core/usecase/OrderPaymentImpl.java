package com.lanchonete.order_payment.core.usecase;


import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.model.PaymentNotification;
import com.lanchonete.order_payment.core.usecase.interfaces.OrderPaymentUseCase;
import com.lanchonete.order_payment.core.usecase.interfaces.PaymentOrderPersistence;
import com.lanchonete.order_payment.core.usecase.interfaces.PaymentGateway;
import com.lanchonete.order_payment.core.usecase.interfaces.QRCodeGeneration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderPaymentImpl implements OrderPaymentUseCase {
    private final PaymentGateway paymentIntegration;
    private final QRCodeGeneration qrCodeGeneration;
    private final PaymentOrderPersistence paymentOrderPersistence;

    @Override
    public byte[] requestPayment(OrderSnackDTO orderSnackRequest) {
        UUID externalReference = UUID.randomUUID();

        QRCodeData qrData = paymentIntegration.requestQrData(orderSnackRequest, externalReference);
        byte[] qrCodeImg = qrCodeGeneration.generateQRCodeImage(qrData.qrData(), 250, 250);
        paymentOrderPersistence.savePaymentOrder(orderSnackRequest, externalReference);
        return qrCodeImg;
    }

    @Override
    public void updatePaymentStatus(PaymentNotification notification) {

    }

}
