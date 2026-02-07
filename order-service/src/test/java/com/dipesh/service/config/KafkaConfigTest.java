package com.dipesh.service.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class KafkaConfigTest
{
    @Test
    void buildsProducerFactoryWithExpectedConfig()
    {
        KafkaConfig config = new KafkaConfig();

        DefaultKafkaProducerFactory<String, Object> factory = (DefaultKafkaProducerFactory<String, Object>) config.producerFactory();
        Map<String, Object> props = factory.getConfigurationProperties();

        assertEquals("localhost:9092", props.get("bootstrap.servers"));
        assertEquals(StringSerializer.class, props.get("key.serializer"));
        assertEquals(JsonSerializer.class, props.get("value.serializer"));
    }

    @Test
    void buildsKafkaTemplate()
    {
        KafkaConfig config = new KafkaConfig();

        KafkaTemplate<String, Object> template = config.kafkaTemplate();

        assertNotNull(template);
    }
}
