package com.fintech.wallet.transfer.system.adapter.out.persistence.adapter;

import com.fintech.wallet.transfer.system.adapter.out.persistence.entity.CustomerEntity;
import com.fintech.wallet.transfer.system.adapter.out.persistence.mapper.CustomerMapper;
import com.fintech.wallet.transfer.system.adapter.out.persistence.repository.CustomerRepository;
import com.fintech.wallet.transfer.system.application.port.out.CustomerRepositoryPort;
import com.fintech.wallet.transfer.system.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerAdapter implements CustomerRepositoryPort {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerAdapter(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity saved = customerRepository.save(customerMapper.toEntity(customer));
        return customerMapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id).map(customerMapper::toDomain);
    }

}
