package dev.babatunde.transferservice.models.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        String accountNumber,
        String accountName,
        String description,
        String status,
        String category,
        String paymentReference,
        String channel,
        String currency,
        BigDecimal amount,
        LocalDateTime date,
        boolean isCommissionWorthy,
        String statusMessage,
        BigDecimal commission,
        BigDecimal fee,
        List<TransactionEntryDTO> transactionEntries) {
}
