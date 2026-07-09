package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.application.port.in.CreateWalletUseCase;
import com.fintech.wallet.transfer.system.application.port.out.CustomerRepositoryPort;
import com.fintech.wallet.transfer.system.application.port.out.WalletRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Customer;
import com.fintech.wallet.transfer.system.domain.model.Wallet;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateWalletService implements CreateWalletUseCase {

    private final WalletRepositoryPort walletRepositoryPort;
    private final CustomerRepositoryPort customerRepositoryPort;

    public CreateWalletService(WalletRepositoryPort walletRepositoryPort, CustomerRepositoryPort customerRepositoryPort) {
        this.walletRepositoryPort = walletRepositoryPort;
        this.customerRepositoryPort = customerRepositoryPort;
    }

    @Override
    public Wallet createWallet(CreateWalletCommand command) {
        Customer customer = customerRepositoryPort.findById(command.ownerId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.CUSTOMER_NOT_FOUND_BY_ID, command.ownerId()));

        Wallet newWallet;
        if (Objects.isNull(command.amount())) {
            newWallet = Wallet.openWalletWithoutBalance(customer, command.currency());
        } else {
            newWallet = Wallet.openWalletWithBalance(customer, command.amount(), command.currency());
        }

        return walletRepositoryPort.save(newWallet);
    }

}
