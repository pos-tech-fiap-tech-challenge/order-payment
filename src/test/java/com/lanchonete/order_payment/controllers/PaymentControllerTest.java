package com.lanchonete.order_payment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanchonete.order_payment.adapters.controllers.PaymentController;
import com.lanchonete.order_payment.adapters.dto.OrderSnackDTO;
import com.lanchonete.order_payment.adapters.dto.OrderSnackItemDTO;
import com.lanchonete.order_payment.adapters.dto.ProductDTO;
import com.lanchonete.order_payment.core.domain.PaymentNotification;
import com.lanchonete.order_payment.core.domain.PaymentNotificationData;
import com.lanchonete.order_payment.core.enums.PaymentGateway;
import com.lanchonete.order_payment.core.usecase.interfaces.in.OrderPaymentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private MockMvc mockMvc;
    private OrderPaymentUseCase paymentUseCase;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentUseCase = Mockito.mock(OrderPaymentUseCase.class);
        paymentController = new PaymentController(paymentUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void requestPayment_ShouldReturnOkWithQrCode() throws Exception {
        byte[] mockQrCode = "mockQRCode".getBytes();
        when(paymentUseCase.requestPayment(any(OrderSnackDTO.class))).thenReturn(mockQrCode);

        OrderSnackDTO orderSnackDTO = new OrderSnackDTO();
        orderSnackDTO.setOrderSnackId(UUID.randomUUID());
        orderSnackDTO.setTotalPrice(BigDecimal.valueOf(30));
        orderSnackDTO.setPaymentGateway(PaymentGateway.MERCADO_PAGO);
        ProductDTO productDTO = new ProductDTO();
        OrderSnackItemDTO orderSnackItemDTO = new OrderSnackItemDTO(
                BigDecimal.TEN,
                productDTO,
                3
        );

        orderSnackDTO.setItems(List.of(orderSnackItemDTO));

        mockMvc.perform(post(PaymentController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderSnackDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(mockQrCode));

        verify(paymentUseCase, times(1)).requestPayment(any(OrderSnackDTO.class));
    }

    @Test
    void updatePaymentStatus_ShouldProcessNotificationSuccessfully() throws Exception {
        PaymentNotification notification = new PaymentNotification(
                new PaymentNotificationData("123")
        );

        doNothing().when(paymentUseCase).updatePaymentStatus(any(PaymentNotification.class));

        mockMvc.perform(post(PaymentController.BASE_URL + "/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(notification)))
                .andExpect(status().isCreated());

        verify(paymentUseCase, times(1)).updatePaymentStatus(any(PaymentNotification.class));
    }

    @Test
    void updatePaymentStatus_ShouldLogErrorWhenNotificationInvalid() throws Exception {
        PaymentNotification notification = new PaymentNotification(
                new PaymentNotificationData("")
        );

        mockMvc.perform(post(PaymentController.BASE_URL + "/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(notification)))
                .andExpect(status().isBadRequest());

        verify(paymentUseCase, never()).updatePaymentStatus(any(PaymentNotification.class));
    }
}

