package com.fintech.wallet.transfer.system.application.port.out;

import com.fintech.wallet.transfer.system.domain.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

}
