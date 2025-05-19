package com.example.buildingblocks.caching.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Serializable> redisTemplate;
//    private final StringRedisTemplate stringRedisTemplate;



    @Override
    public <T extends Serializable> void put(String cacheName, String key, T value) {
        redisTemplate.opsForValue().set(formatKey(cacheName, key), value);
    }

    @Override
    public <T extends Serializable> void put(String cacheName, String key, T value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(formatKey(cacheName, key), value, ttl, unit);
    }

    @Nullable
    @Override
    public <T extends Serializable> T get(String cacheName, String key) {
        return (T) redisTemplate.opsForValue().get(formatKey(cacheName, key));
    }

    @Override
    public void evict(String cacheName, String key) {
        redisTemplate.delete(formatKey(cacheName, key));
    }

    @Override
    public void clearCache(String cacheName) {
        Set<String> keys = redisTemplate.keys(cacheName + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Nullable
    @Override
    public <T extends Serializable> Map<String, T> multiGet(String cacheName, Collection<String> keys) {
        List<String> formattedKeys = keys.stream()
                .map(k -> formatKey(cacheName, k))
                .toList();

        List<T> values = (List<T>) redisTemplate.opsForValue().multiGet(formattedKeys);
        if (values == null) return null;

        Map<String, T> result = new HashMap<>();
        Iterator<String> keyIter = keys.iterator();
        Iterator<T> valueIter = values.iterator();

        while (keyIter.hasNext() && valueIter.hasNext()) {
            result.put(keyIter.next(), valueIter.next());
        }
        return result;
    }

    @Override
    public <T extends Serializable> void multiPut(String cacheName, Map<String, T> entries) {
        RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer<T> valueSerializer = (RedisSerializer<T>) redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            entries.forEach((key, value) -> {
                byte[] rawKey = keySerializer.serialize(formatKey(cacheName, key));
                byte[] rawValue = valueSerializer.serialize(value);
                connection.stringCommands().set(rawKey, rawValue);
            });
            return null;
        });
    }

    @Override
    public long deleteByPattern(String cacheName, String pattern) {
        String fullPattern = formatKey(cacheName, pattern);
        Set<String> keys = redisTemplate.keys(fullPattern);
        if (keys != null && !keys.isEmpty()) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }

    @Override
    public <T extends Serializable> T getOrElse(String cacheName, String key, Supplier<T> supplier) {
        T value = get(cacheName, key);
        if (value == null) {
            value = supplier.get();
            put(cacheName, key, value);
        }
        return value;
    }

    @Override
    public <T extends Serializable> T getOrElse(String cacheName, String key, Supplier<T> supplier, long ttl, TimeUnit unit) {
        T value = get(cacheName, key);
        if (value == null) {
            value = supplier.get();
            put(cacheName, key, value, ttl, unit);
        }
        return value;
    }

    @Override
    public boolean exists(String cacheName, String key) {
        Boolean result = redisTemplate.hasKey(formatKey(cacheName, key));
        return result != null && result;
    }

    @Override
    public boolean expire(String cacheName, String key, long ttl, TimeUnit unit) {
        Boolean result = redisTemplate.expire(formatKey(cacheName, key), ttl, unit);
        return result != null && result;
    }

    @Nullable
    @Override
    public Long getRemainingTtl(String cacheName, String key, TimeUnit unit) {
        Long seconds = redisTemplate.getExpire(formatKey(cacheName, key));
        return seconds != null && seconds > 0 ? unit.convert(seconds, TimeUnit.SECONDS) : null;
    }

    @Nullable
    @Override
    public <T extends Serializable> T hashGet(String cacheName, String key, Object field) {
        return (T) redisTemplate.opsForHash().get(formatKey(cacheName, key), field);
    }

    @Override
    public <T extends Serializable> void hashPut(String cacheName, String key, Object field, T value) {
        redisTemplate.opsForHash().put(formatKey(cacheName, key), field, value);
    }

    @Override
    public long hashDelete(String cacheName, String key, Object... fields) {
        return redisTemplate.opsForHash().delete(formatKey(cacheName, key), fields);
    }

//    @Nullable
//    @Override
//    public Set<? extends Serializable> zSetRange(String cacheName, String key, long start, long end) {
//        return redisTemplate.opsForZSet().range(formatKey(cacheName, key), start, end);
//    }
//
//    @Override
//    public <T extends Serializable> void zSetAdd(String cacheName, String key, T value, double score) {
//        redisTemplate.opsForZSet().add(formatKey(cacheName, key), value, score);
//    }

    private String formatKey(String cacheName, String key) {
        return cacheName + "::" + key;
    }
}