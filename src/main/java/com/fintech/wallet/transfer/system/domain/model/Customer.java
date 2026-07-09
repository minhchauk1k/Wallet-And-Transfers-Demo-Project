package com.fintech.wallet.transfer.system.domain.model;

import com.fintech.wallet.transfer.system.domain.common.UUIDv7;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Customer {

    private final UUID id;
    private String fullName;

    /**
     * Private constructor initializing the customer.
     *
     * @param id       customer UUID identifier
     * @param fullName customer's full name
     */
    private Customer(UUID id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    /**
     * Factory method to create a Customer instance with the provided id and full name.
     *
     * @param id       customer UUID identifier
     * @param fullName customer's full name
     * @return new Customer instance
     */
    public static Customer of(UUID id, String fullName) {
        return new Customer(id, fullName);
    }

    /**
     * Factory method to create a new Customer with auto-generated UUID.
     * Convenience method when caller does not provide an explicit id.
     *
     * @param fullName customer's full name
     * @return new Customer instance with generated UUID
     */
    public static Customer create(String fullName) {
        return new Customer(UUIDv7.randomUUID(), fullName);
    }

    /**
     * Updates the customer's full name.
     *
     * @param fullName new full name for the customer
     */
    public void changeFullName(String fullName) {
        this.fullName = fullName;
    }
}
