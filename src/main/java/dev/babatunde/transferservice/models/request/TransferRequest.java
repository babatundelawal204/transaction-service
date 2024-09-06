package dev.babatunde.transferservice.models.request;

import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Provide a valid transfer reference")
        @Size(max = 80, message = "transfer reference cannot length cannot be more than 80")
        String paymentReference,

        @NotNull(message = "Enter transfer amount")
        @Min(value = 1, message = "transfer amount cannot be less than 1")
        BigDecimal amount,

        @NotNull(message = "Select a valid channel for transaction")
        TransactionChannel channel,

        @Size(max = 1000, message = "description cannot be more than 1000 characters")
        String description,

        @NotNull(message = "Select valid transaction type")
        TransactionCategory type,

        @NotBlank(message = "select valid currency")
        @Size(min = 3, max=3, message = "currency cannot be less or more than 3")
        String currency,

        @NotBlank(message = "destination account number cannot be blank")
        @Size(min = 10, max=10, message = "destination account number cannot be less or more than 10")
        String destinationAccountNumber,

        @NotBlank(message = "source account number cannot be blank")
        @Size(min = 10, max=10, message = "sender account number cannot be less or more than 10")
        String sourceAccountNumber
) {
}
