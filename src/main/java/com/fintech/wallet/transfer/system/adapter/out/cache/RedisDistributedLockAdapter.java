package com.fintech.wallet.transfer.system.adapter.out.cache;

import com.fintech.wallet.transfer.system.adapter.constant.AdapterConstant;
import com.fintech.wallet.transfer.system.application.port.out.DistributedLockPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisDistributedLockAdapter implements DistributedLockPort {

    /* default value: 5s */
    @Value("${spring.configuration.redis.idempotency.lock-ttl:5000}")
    private long lockTtl;

    private final Duration LOCK_TTL = Duration.ofMillis(lockTtl);

    private final StringRedisTemplate stringRedisTemplate;

    public RedisDistributedLockAdapter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(String lockKey) {
        return stringRedisTemplate.opsForValue().setIfAbsent(buildLockKey(lockKey), lockKey, LOCK_TTL);
    }

    @Override
    public void releaseLock(String lockKey) {
        stringRedisTemplate.delete(buildLockKey(lockKey));
    }

    private String buildLockKey(String key) {
        return AdapterConstant.REDIS_DISTRIBUTED_LOCK_KEY + key;
    }

}
