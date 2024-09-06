package dev.babatunde.transferservice.models.response;

import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionSummaryResponse(
        BigDecimal totalAmount,
        TransactionChannel channel,
        LocalDate transactionDate,
        BigDecimal totalCommission,
        TransactionType type
) {
}
