package com.lanchonete.order_payment.adapters.repositories;

import com.lanchonete.order_payment.adapters.entities.PaymentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {

    @Query("{ 'orderId' : ?0 }")
    PaymentEntity findPaymentByOrderId(String orderId);
}
