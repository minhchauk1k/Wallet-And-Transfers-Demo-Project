package com.fintech.wallet.transfer.system.adapter.out.persistence.mapper;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.CustomerEntity;
import com.fintech.wallet.transfer.system.domain.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerEntity toEntity(Customer customer) {
        return new CustomerEntity(
                customer.getId(),
                customer.getFullName()
        );
    }

    public Customer toDomain(CustomerEntity customerEntity) {
        return Customer.of(
                customerEntity.getId(),
                customerEntity.getFullName()
        );
    }

}
