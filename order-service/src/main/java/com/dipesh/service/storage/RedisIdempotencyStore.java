package com.dipesh.service.storage;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class RedisIdempotencyStore implements IdempotencyStore
{

    private static final String PREFIX = "idem:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate redis;

    public RedisIdempotencyStore(StringRedisTemplate redis)
    {
        this.redis = redis;
    }

    @Override
    public Optional<String> get(String key)
    {
        return Optional.ofNullable(redis.opsForValue().get(buildKey(key)));
    }

    @Override
    public void put(String key, String orderId)
    {

        Boolean success = redis.opsForValue().setIfAbsent(buildKey(key), orderId, TTL);

        // success == false means duplicate request
        // we intentionally do nothing
    }

    private String buildKey(String key)
    {
        return PREFIX + key;
    }
}
