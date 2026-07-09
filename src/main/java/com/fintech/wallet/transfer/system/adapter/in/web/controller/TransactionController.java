package com.fintech.wallet.transfer.system.adapter.in.web.controller;

import com.fintech.wallet.transfer.system.adapter.in.web.dto.TransactionRequest;
import com.fintech.wallet.transfer.system.application.constant.ApplicationConst;
import com.fintech.wallet.transfer.system.application.port.in.TransferMoneyUseCase;
import com.fintech.wallet.transfer.system.domain.model.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransferMoneyUseCase transferMoneyUseCase;

    public TransactionController(TransferMoneyUseCase transferMoneyUseCase) {
        this.transferMoneyUseCase = transferMoneyUseCase;
    }

    @PostMapping
    public ResponseEntity<?> transfer(
            @RequestHeader(ApplicationConst.HEADER_IDEMPOTENCY_KEY) String idempotencyKey,
            @Valid @RequestBody TransactionRequest request
    ) {

        Transaction transaction;

        switch (request.type()) {
            case "transfer" -> {
                transaction = transferMoneyUseCase.transfer(
                        new TransferMoneyUseCase.TransferTransactionCommand(
                                idempotencyKey,
                                request.sourceWalletNumber(),
                                request.targetWalletNumber(),
                                request.amount(),
                                request.currency(),
                                request.description()
                        )
                );
            }
            default -> throw new IllegalArgumentException("Invalid transfer type: " + request.type());
        }

        return ResponseEntity.ok().body(transaction);
    }

}
