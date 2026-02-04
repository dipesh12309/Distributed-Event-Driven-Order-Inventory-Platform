package com.dipesh.service.storage;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisProcessedEventStore implements ProcessedEventStore
{
    private static final String PREFIX = "event:";
    private static final Duration TTL = Duration.ofHours(6);

    private final StringRedisTemplate redis;

    public RedisProcessedEventStore(StringRedisTemplate redis)
    {
        this.redis = redis;
    }

    @Override
    public boolean isDuplicate(String consumer, String eventKey)
    {
        String key = PREFIX + consumer + ":" + eventKey;
        Boolean stored = redis.opsForValue().setIfAbsent(key, "1", TTL);
        return stored == null || !stored;
    }
}
