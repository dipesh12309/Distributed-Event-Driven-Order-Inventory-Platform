package com.dipesh.service.events;

import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.repo.OrderRepository;
import com.dipesh.service.storage.ProcessedEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InventoryReservationFailedConsumer
{
    private static final String CONSUMER_ID = "inventory-failed-order";

    private final OrderRepository orderRepository;
    private final ProcessedEventStore processedEventStore;

    @KafkaListener(topics = "inventory-reservation-failed-topic", groupId = "order-group")
    @Transactional
    public void consume(InventoryReservationFailedEvent event, Acknowledgment acknowledgment)
    {
        if (processedEventStore.isDuplicate(CONSUMER_ID, event.orderId()))
        {
            acknowledgment.acknowledge();
            return;
        }

        OrderEntity entity = orderRepository.findById(event.orderId()).orElseThrow();
        entity.markCancelled();
        acknowledgment.acknowledge();
    }
}
