package com.fintech.wallet.transfer.system.adapter.in.web.controller;

import com.fintech.wallet.transfer.system.adapter.out.cache.RedisDistributedLockAdapter;
import com.fintech.wallet.transfer.system.adapter.out.cache.RedisIdempotencyKeyAdapter;
import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.application.port.in.TransferMoneyUseCase;
import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/v1/test-concurrency-safety")
public class TestConcurrencySafetyController {

    private final TransferMoneyUseCase transferMoneyUseCase;
    private final RedisDistributedLockAdapter redisDistributedLockAdapter;
    private final RedisIdempotencyKeyAdapter redisIdempotencyKeyAdapter;
    private final ObjectMapper objectMapper;

    public TestConcurrencySafetyController(TransferMoneyUseCase transferMoneyUseCase, RedisDistributedLockAdapter redisDistributedLockAdapter, RedisIdempotencyKeyAdapter redisIdempotencyKeyAdapter, ObjectMapper objectMapper) {
        this.transferMoneyUseCase = transferMoneyUseCase;
        this.redisDistributedLockAdapter = redisDistributedLockAdapter;
        this.redisIdempotencyKeyAdapter = redisIdempotencyKeyAdapter;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/different-request")
    public ResponseEntity<?> testConcurrencySafetyDiffRequest(@RequestParam int numberOfRequest) {
        Map<String, Integer> resultMaps = new ConcurrentHashMap<>();
        numberOfRequest = numberOfRequest != 0 ? numberOfRequest : 1000;

        // test with [VirtualThreadPerTask]
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfRequest; i++) {
                executor.execute(() -> {
                    // N request with unique [idempotencyKey]
                    String idempotencyKey = UUIDv7.randomUUID().toString();
                    TransferMoneyUseCase.TransferTransactionCommand command = new TransferMoneyUseCase.TransferTransactionCommand(
                            idempotencyKey,
                            "57511783565900642",
                            "57511783565793344",
                            BigDecimal.ONE,
                            "USD",
                            idempotencyKey + " description"
                    );
                    try {
                        Transaction result = transferMoneyUseCase.transfer(command);
                        resultMaps.merge(result.getStatus().name(), 1, Integer::sum);
                    } catch (Exception exception) {
                        resultMaps.merge(exception.getClass().getCanonicalName() + ": " + exception.getMessage(), 1, Integer::sum);
                        throw exception;
                    }
                    // End of the [Task]
                });
            }
        }

        return ResponseEntity.ok(resultMaps);
    }

    @GetMapping("/same-request")
    public ResponseEntity<?> testConcurrencySafetySameRequest(@RequestParam int numberOfRequest) {
        Map<String, Integer> resultMaps = new ConcurrentHashMap<>();
        numberOfRequest = numberOfRequest != 0 ? numberOfRequest : 1000;

        // N request with the same [idempotencyKey]
        String idempotencyKey = UUIDv7.randomUUID().toString();

        // test with [VirtualThreadPerTask]
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfRequest; i++) {
                executor.execute(() -> {
                    // Start of the [Task]
                    // TESTCASE #1: N request with the same [idempotencyKey]
                    try {
                        boolean isLocked = redisDistributedLockAdapter.tryLock(idempotencyKey);
                        if (!isLocked) {
                            throw new ApplicationException(ApplicationError.TRANSACTION_IN_PROGRESS);
                        }

                        Optional<String> cached = redisIdempotencyKeyAdapter.getResult(idempotencyKey);
                        if (cached.isPresent()) {
                            resultMaps.merge(objectMapper.readValue(cached.get(), Transaction.class).getStatus().name() + " cache hit!!", 1, Integer::sum);

                            redisDistributedLockAdapter.releaseLock(idempotencyKey);
                            return;
                        }
                    } catch (Exception exception) {
                        resultMaps.merge(exception.getClass().getCanonicalName() + ": " + exception.getMessage(), 1, Integer::sum);
                        throw exception;
                    }

                    TransferMoneyUseCase.TransferTransactionCommand command = new TransferMoneyUseCase.TransferTransactionCommand(
                            idempotencyKey,
                            "57511783565900642",
                            "57511783565793344",
                            BigDecimal.ONE,
                            "USD",
                            idempotencyKey + " description"
                    );
                    try {
                        Transaction result = transferMoneyUseCase.transfer(command);
                        resultMaps.merge(result.getStatus().name(), 1, Integer::sum);
                    } catch (Exception exception) {
                        resultMaps.merge(exception.getClass().getCanonicalName() + ": " + exception.getMessage(), 1, Integer::sum);
                        throw exception;
                    }
                    // End of the [Task]
                });
            }
        }

        return ResponseEntity.ok(resultMaps);
    }
}
