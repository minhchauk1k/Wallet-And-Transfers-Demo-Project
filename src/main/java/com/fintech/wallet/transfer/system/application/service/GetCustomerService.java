package com.fintech.wallet.transfer.system.application.service;

import com.fintech.wallet.transfer.system.application.constant.ApplicationError;
import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.application.port.in.GetCustomerUseCase;
import com.fintech.wallet.transfer.system.application.port.out.CustomerRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerService implements GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public GetCustomerService(CustomerRepositoryPort customerRepositoryPort) {
        this.customerRepositoryPort = customerRepositoryPort;
    }

    @Override
    public Customer getCustomer(GetCustomerCommand command) {
        return customerRepositoryPort.findById(command.id())
                .orElseThrow(() -> new ApplicationException(ApplicationError.CUSTOMER_NOT_FOUND_BY_ID, command.id()));
    }

}
