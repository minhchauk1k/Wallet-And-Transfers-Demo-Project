package com.fintech.wallet.transfer.system.adapter.in.web.controller;

import com.fintech.wallet.transfer.system.adapter.in.web.dto.CreateWalletRequest;
import com.fintech.wallet.transfer.system.application.port.in.CreateWalletUseCase;
import com.fintech.wallet.transfer.system.application.port.in.GetWalletUseCase;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final GetWalletUseCase getWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase, GetWalletUseCase getWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
        this.getWalletUseCase = getWalletUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = createWalletUseCase.createWallet(
                new CreateWalletUseCase.CreateWalletCommand(
                        request.ownerId(),
                        request.balance(),
                        request.currency()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getWallet(@PathVariable UUID walletId) {
        Wallet wallet = getWalletUseCase.getWallet(
                new GetWalletUseCase.GetWalletCommand(walletId)
        );

        return ResponseEntity.ok().body(wallet);
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<?> getWalletBalanceById(@PathVariable UUID walletId) {
        Wallet wallet = getWalletUseCase.getWalletBalanceById(
                new GetWalletUseCase.GetWalletBalanceCommand(walletId)
        );

        return ResponseEntity.ok().body(
                new GetWalletUseCase.GetWalletBalanceResult(
                        wallet.getId(),
                        wallet.getWalletNumber().getValue(),
                        wallet.getBalance()
                )
        );
    }

    @GetMapping("/wallet-number/{walletNumber}/balance")
    public ResponseEntity<?> getWalletBalanceByWalletNumber(@PathVariable String walletNumber) {
        Wallet wallet = getWalletUseCase.getWalletBalanceByWalletNumber(
                new GetWalletUseCase.GetWalletBalanceByWalletNumberCommand(walletNumber)
        );

        return ResponseEntity.ok().body(
                new GetWalletUseCase.GetWalletBalanceResult(
                        wallet.getId(),
                        wallet.getWalletNumber().getValue(),
                        wallet.getBalance()
                )
        );
    }
}
