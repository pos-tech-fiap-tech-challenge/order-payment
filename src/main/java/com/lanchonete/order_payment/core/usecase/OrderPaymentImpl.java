package com.lanchonete.order_payment.core.usecase;


import com.lanchonete.order_payment.adapters.dto.OrderDTO;
import com.lanchonete.order_payment.adapters.dto.QRCodeData;
import com.lanchonete.order_payment.core.domain.Payment;
import com.lanchonete.order_payment.core.enums.PaymentStatus;
import com.lanchonete.order_payment.core.domain.OrderSnackPaymentStatus;
import com.lanchonete.order_payment.core.domain.PaymentNotification;
import com.lanchonete.order_payment.core.usecase.interfaces.in.OrderPaymentUseCase;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentOrderRepository;
import com.lanchonete.order_payment.core.usecase.interfaces.out.PaymentGateway;
import com.lanchonete.order_payment.core.usecase.interfaces.out.QRCodeGenerationGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderPaymentImpl implements OrderPaymentUseCase {
    private final PaymentGateway paymentGateway;
    private final QRCodeGenerationGateway qrCodeGenerationGateway;
    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public byte[] requestPayment(OrderDTO orderSnackRequest) {
        QRCodeData qrData = paymentGateway.requestQrData(orderSnackRequest);
        byte[] qrCodeImg = qrCodeGenerationGateway.generateQRCodeImage(qrData.qrData(), 250, 250);

        var paymentSave = Payment
                .builder()
                .paymentGateway(orderSnackRequest.getPaymentGateway())
                .paymentStatus(PaymentStatus.OPPENED)
                .orderSnackId(orderSnackRequest.getOrderId())
                .build();

        paymentOrderRepository.savePaymentOrder(paymentSave);
        return qrCodeImg;
    }

    @Override
    public void updatePaymentStatus(PaymentNotification notification) {
        try {
            OrderSnackPaymentStatus mercadoPagoOrderData = paymentGateway.getOrderData(notification.data().id());
            var paymentUpdate = paymentOrderRepository.findPaymentByOrderId(mercadoPagoOrderData.getExternalOrderId());
            paymentUpdate.setPaymentStatus(generatePaymentStatus(mercadoPagoOrderData.getPaymentStatus()));

            paymentOrderRepository.savePaymentOrder(paymentUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Error updating payment status: " + e.getMessage());
        }
    }


    private PaymentStatus generatePaymentStatus(String status) {
        return switch (status) {
            case "approved" -> PaymentStatus.APPROVED;
            case "opened" -> PaymentStatus.OPPENED;
            case "expired" -> PaymentStatus.EXPIRED;
            default ->
                    throw new UnsupportedOperationException("Payment status" + status + " not supported, choose between: closed, opened or expired");
        };
    }
}
