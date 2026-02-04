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
public class PaymentResultConsumer
{
    private static final String CONSUMER_ID = "payment-result";

    private final OrderRepository repository;
    private final InventoryEventProducer inventoryEventProducer;
    private final ProcessedEventStore processedEventStore;

    @KafkaListener(topics = "payment-result-topic", groupId = "order-group")
    @Transactional
    public void consume(PaymentResultEvent event, Acknowledgment acknowledgment)
    {

        if (processedEventStore.isDuplicate(CONSUMER_ID, event.orderId()))
        {
            acknowledgment.acknowledge();
            return;
        }

        OrderEntity entity = repository.findById(event.orderId()).orElseThrow();

        if (event.success())
        {
            entity.markConfirmed();
        }
        else
        {
            entity.markCancelled();
            inventoryEventProducer.sendInventoryRelease(new InventoryReleaseEvent(
                    entity.getOrderId(),
                    entity.getItems().stream()
                            .map(item -> new OrderItemPayload(item.getProductId(), item.getQuantity()))
                            .toList()
            ));
        }
        acknowledgment.acknowledge();
    }
}
