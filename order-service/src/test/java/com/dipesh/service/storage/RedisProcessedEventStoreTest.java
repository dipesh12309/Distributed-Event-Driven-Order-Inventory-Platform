package com.dipesh.service.storage;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisProcessedEventStoreTest
{
    @Test
    void returnsTrueWhenDuplicate()
    {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(ops);
        when(ops.setIfAbsent("event:consumer:order", "1", Duration.ofHours(6))).thenReturn(false);

        RedisProcessedEventStore store = new RedisProcessedEventStore(redis);

        assertTrue(store.isDuplicate("consumer", "order"));
    }

    @Test
    void returnsFalseWhenStored()
    {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(ops);
        when(ops.setIfAbsent("event:consumer:order", "1", Duration.ofHours(6))).thenReturn(true);

        RedisProcessedEventStore store = new RedisProcessedEventStore(redis);

        assertFalse(store.isDuplicate("consumer", "order"));
    }
}
