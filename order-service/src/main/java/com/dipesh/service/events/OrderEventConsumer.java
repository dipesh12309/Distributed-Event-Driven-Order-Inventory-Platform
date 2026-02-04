package com.dipesh.service.events;

import com.dipesh.service.payment.PaymentProcessor;
import com.dipesh.service.storage.ProcessedEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer
{

    private static final String CONSUMER_ID = "payment-processor";

    private final PaymentProcessor paymentProcessor;
    private final ProcessedEventStore processedEventStore;

    @KafkaListener(topics = "inventory-reserved-topic", groupId = "payment-group")
    public void consume(InventoryReservedEvent event, Acknowledgment acknowledgment)
    {
        if (processedEventStore.isDuplicate(CONSUMER_ID, event.orderId()))
        {
            acknowledgment.acknowledge();
            return;
        }
        paymentProcessor.process(new OrderCreatedEvent(event.orderId(), event.userId(), event.amount(), event.items()));
        acknowledgment.acknowledge();
    }
}
