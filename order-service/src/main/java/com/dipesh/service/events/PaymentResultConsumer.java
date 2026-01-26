package com.dipesh.service.events;

import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentResultConsumer
{

    private final OrderRepository repository;

    @KafkaListener(topics = "payment-result-topic", groupId = "order-group")
    @Transactional
    public void consume(PaymentResultEvent event)
    {

        OrderEntity entity = repository.findById(event.orderId()).orElseThrow();

        if (event.success())
        {
            entity.markConfirmed();
        }
        else
        {
            entity.markCancelled();
        }
    }
}
