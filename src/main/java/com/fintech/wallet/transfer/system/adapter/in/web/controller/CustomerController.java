package com.fintech.wallet.transfer.system.adapter.in.web.controller;

import com.fintech.wallet.transfer.system.adapter.in.web.dto.CreateCustomerRequest;
import com.fintech.wallet.transfer.system.application.port.in.CreateCustomerUseCase;
import com.fintech.wallet.transfer.system.application.port.in.GetCustomerUseCase;
import com.fintech.wallet.transfer.system.domain.model.Customer;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetCustomerUseCase getCustomerUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer customer = createCustomerUseCase.createCustomer(
                new CreateCustomerUseCase.CreateCustomerCommand(
                        request.fullName()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomer(@PathVariable UUID customerId) {
        Customer customer = getCustomerUseCase.getCustomer(
                new GetCustomerUseCase.GetCustomerCommand(customerId)
        );

        return ResponseEntity.ok().body(customer);
    }
}
