package com.fintech.wallet.transfer.system.adapter.constant;

public class AdapterConstant {

    /* Redis configuration */
    public static final String REDIS_DISTRIBUTED_LOCK_KEY = "lock:distributed-lock:";
    public static final String REDIS_CACHE_IDEMPOTENCY_KEY = "cache:idempotency-key:";

    /* Request header configuration */
    public static final String HEADER_IDEMPOTENCY_KEY = "Idempotency-Key";

}
