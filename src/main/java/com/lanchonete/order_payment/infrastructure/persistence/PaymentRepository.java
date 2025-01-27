package com.lanchonete.order_payment.infrastructure.persistence;

import com.lanchonete.order_payment.adapters.entities.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, UUID> {
}
