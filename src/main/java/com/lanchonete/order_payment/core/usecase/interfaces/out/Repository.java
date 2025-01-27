package com.lanchonete.order_payment.core.usecase.interfaces.out;

public interface Repository<T> {
    void save(T entity);
}
