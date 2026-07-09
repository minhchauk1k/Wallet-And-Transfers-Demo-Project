package com.fintech.wallet.transfer.system.application.port.in;

import com.fintech.wallet.transfer.system.domain.model.Customer;

import java.util.UUID;

public interface GetCustomerUseCase {

    Customer getCustomer(GetCustomerCommand command);

    record GetCustomerCommand(UUID id) {
    }

}
