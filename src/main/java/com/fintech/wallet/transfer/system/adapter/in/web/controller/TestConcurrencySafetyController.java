package com.fintech.wallet.transfer.system.adapter.in.web.controller;

import com.fintech.wallet.transfer.system.application.port.in.TransferMoneyUseCase;
import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/v1/test-concurrency-safety")
public class TestConcurrencySafetyController {

    private final TransferMoneyUseCase transferMoneyUseCase;

    public TestConcurrencySafetyController(TransferMoneyUseCase transferMoneyUseCase) {
        this.transferMoneyUseCase = transferMoneyUseCase;
    }

    @GetMapping
    public ResponseEntity<?> testConcurrencySafety(@RequestParam int numberOfRequest) {
        Map<String, Integer> resultMaps = new ConcurrentHashMap<>();
        numberOfRequest = numberOfRequest != 0 ? numberOfRequest : 1000;

        // test with [VirtualThreadPerTask]
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfRequest; i++) {
                executor.execute(() -> {
                    // Start of the [Task]
                    String key = UUIDv7.randomUUID().toString();
                    TransferMoneyUseCase.TransferTransactionCommand command = new TransferMoneyUseCase.TransferTransactionCommand(
                            key,
                            "57511783565900642",
                            "57511783565793344",
                            BigDecimal.ONE,
                            "USD",
                            key
                    );
                    try {
                        Transaction result = transferMoneyUseCase.transfer(command);
                        resultMaps.merge(result.getStatus().name(), 1, Integer::sum);
                    } catch (Exception exception) {
                        resultMaps.merge(exception.getClass().getCanonicalName() + ": " + exception.getMessage(), 1, Integer::sum);
                    }
                    // End of the [Task]
                });
            }
        }

        return ResponseEntity.ok(resultMaps);
    }
}
