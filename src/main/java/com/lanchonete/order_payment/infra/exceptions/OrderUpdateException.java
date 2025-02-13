package com.lanchonete.order_payment.infra.exceptions;

public class OrderUpdateException extends RuntimeException{
    public OrderUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
