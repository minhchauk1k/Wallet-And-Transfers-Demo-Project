package com.fintech.wallet.transfer.system.domain.common;

import com.fasterxml.uuid.Generators;
import com.fintech.wallet.transfer.system.domain.constant.BusinessError;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public final class UUIDv7 {

    private final UUID value;

    /**
     * Private constructor validating input UUID.
     *
     * @param value UUID value (must be non-null and non-empty string)
     * @throws BusinessException if value is null or blank
     */
    private UUIDv7(UUID value) {
        /* check null or empty or white space */
        if (value == null || value.toString().isBlank()) {
            throw new BusinessException(BusinessError.ID_NULL_OR_EMPTY);
        }

        this.value = value;
    }

    /**
     * Generates a new UUID version 7 using the time-based epoch random generator.
     *
     * @return newly generated UUID
     */
    public static UUID randomUUID() {
        /* Generate UUID version 7 */
        return new UUIDv7(Generators.timeBasedEpochRandomGenerator().generate()).getValue();
    }
}
