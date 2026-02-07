package com.dipesh.service.storage;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisIdempotencyStoreTest
{
    @Test
    void getReturnsOptionalValue()
    {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(ops);
        when(ops.get("idem:key-1")).thenReturn("order-1");

        RedisIdempotencyStore store = new RedisIdempotencyStore(redis);

        Optional<String> result = store.get("key-1");

        assertTrue(result.isPresent());
        assertEquals("order-1", result.get());
    }

    @Test
    void putStoresOnlyIfAbsent()
    {
        StringRedisTemplate redis = mock(StringRedisTemplate.class);
        ValueOperations<String, String> ops = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(ops);

        RedisIdempotencyStore store = new RedisIdempotencyStore(redis);
        store.put("key-2", "order-2");

        verify(ops).setIfAbsent("idem:key-2", "order-2", Duration.ofMinutes(10));
    }
}
