package com.fintech.wallet.transfer.system.adapter.out.cache;

import com.fintech.wallet.transfer.system.adapter.constant.AdapterConstant;
import com.fintech.wallet.transfer.system.application.port.out.IdempotencyKeyPort;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
public class RedisIdempotencyKeyAdapter implements IdempotencyKeyPort {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    /* default value: 5s */
    @Value("${spring.configuration.redis.idempotency.lock-ttl:5000}")
    private long lockTtl;

    /* default value: 24h */
    @Value("${spring.configuration.redis.idempotency.cache-ttl:86400000}")
    private long cacheTtl;

    private final Duration LOCK_TTL = Duration.ofMillis(lockTtl);
    private final Duration CACHE_TTL = Duration.ofMillis(cacheTtl);

    public RedisIdempotencyKeyAdapter(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean tryLock(String idempotencyKey) {
        return stringRedisTemplate.opsForValue().setIfAbsent(buildLockKey(idempotencyKey), idempotencyKey, LOCK_TTL);
    }

    @Override
    public void releaseLock(String idempotencyKey) {
        stringRedisTemplate.delete(buildLockKey(idempotencyKey));
    }

    @Override
    public Optional<String> getResult(String idempotencyKey) {
        String json = stringRedisTemplate.opsForValue().get(buildCacheKey(idempotencyKey));
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(json);
        } catch (Exception exception) {
            log.error("Failed to parse JSON string as Object: {}", exception.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void saveResult(String idempotencyKey, Transaction transaction) {
        try {
            String json = objectMapper.writeValueAsString(transaction);
            stringRedisTemplate.opsForValue().setIfAbsent(buildCacheKey(idempotencyKey), json, CACHE_TTL);
        } catch (Exception exception) {
            log.error("Failed to parse Object as JSON string: {}", exception.getMessage());
        }
    }

    private String buildLockKey(String key) {
        return AdapterConstant.REDIS_LOCK_IDEMPOTENCY_KEY + key;
    }

    private String buildCacheKey(String key) {
        return AdapterConstant.REDIS_CACHE_IDEMPOTENCY_KEY + key;
    }
}
