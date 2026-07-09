package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.port.in.CreateCustomerUseCase;
import com.fintech.wallet.transfer.system.application.port.out.CustomerRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public CreateCustomerService(CustomerRepositoryPort customerRepositoryPort) {
        this.customerRepositoryPort = customerRepositoryPort;
    }

    @Override
    public Customer createCustomer(CreateCustomerCommand command) {
        Customer newCustomer = Customer.create(command.fullName());

        return customerRepositoryPort.save(newCustomer);
    }

}
