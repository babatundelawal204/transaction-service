package dev.babatunde.transferservice.models.response;

import java.math.BigDecimal;

public record TransactionEntryDTO(
        String accountNumber,
        String accountName,
        String type,
        BigDecimal amount
) {
}
