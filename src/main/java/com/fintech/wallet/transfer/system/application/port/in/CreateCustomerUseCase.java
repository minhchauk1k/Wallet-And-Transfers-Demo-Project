package com.fintech.wallet.transfer.system.application.port.in;

import com.fintech.wallet.transfer.system.domain.model.Customer;

public interface CreateCustomerUseCase {

    Customer createCustomer(CreateCustomerCommand command);

    record CreateCustomerCommand(String fullName) {
    }

}
