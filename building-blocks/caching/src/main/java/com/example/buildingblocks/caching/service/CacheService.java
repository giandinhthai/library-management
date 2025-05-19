package com.example.buildingblocks.caching.service;

import jakarta.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface CacheService {

    // Basic CRUD Operations
    <T extends Serializable> void put(String cacheName, String key, T value);
    <T extends Serializable> void put(String cacheName, String key, T value, long ttl, TimeUnit unit);

    @Nullable
    <T extends Serializable> T get(String cacheName, String key);

    void evict(String cacheName, String key);
    void clearCache(String cacheName);

    // Bulk Operations
    @Nullable
    <T extends Serializable> Map<String, T> multiGet(String cacheName, Collection<String> keys);
    <T extends Serializable> void multiPut(String cacheName, Map<String, T> entries);
    long deleteByPattern(String cacheName, String pattern);

    // Advanced Patterns
    <T extends Serializable> T getOrElse(String cacheName, String key, Supplier<T> supplier);
    <T extends Serializable> T getOrElse(String cacheName, String key, Supplier<T> supplier, long ttl, TimeUnit unit);

    // Utility Methods
    boolean exists(String cacheName, String key);
    boolean expire(String cacheName, String key, long ttl, TimeUnit unit);
    @Nullable
    Long getRemainingTtl(String cacheName, String key, TimeUnit unit);

    // Hash Operations
    @Nullable
    <T extends Serializable> T hashGet(String cacheName, String key, Object field);
    <T extends Serializable> void hashPut(String cacheName, String key, Object field, T value);
    long hashDelete(String cacheName, String key, Object... fields);

    // Sorted Set Operations
//    @Nullable
//    Set<? extends Serializable> zSetRange(String cacheName, String key, long start, long end);
//    <T extends Serializable> void zSetAdd(String cacheName, String key, T value, double score);
}