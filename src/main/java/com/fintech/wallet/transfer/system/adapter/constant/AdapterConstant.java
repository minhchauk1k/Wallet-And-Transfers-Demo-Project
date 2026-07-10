package com.fintech.wallet.transfer.system.adapter.constant;

public class AdapterConstant {

    /* Redis configuration */
    public static final String REDIS_LOCK_IDEMPOTENCY_KEY = "lock:idempotency-key:";
    public static final String REDIS_CACHE_IDEMPOTENCY_KEY = "cache:idempotency-key:";

    /* Request header configuration */
    public static final String HEADER_IDEMPOTENCY_KEY = "Idempotency-Key";

}
