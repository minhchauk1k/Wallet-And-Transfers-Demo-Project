package com.fintech.wallet.transfer.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class WalletTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletTransferApplication.class, args);
    }

}
