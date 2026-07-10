package com.fintech.wallet.transfer.system.application.port.out;

public interface DistributedLockPort {

    boolean tryLock(String lockKey);

    void releaseLock(String lockKey);

}
