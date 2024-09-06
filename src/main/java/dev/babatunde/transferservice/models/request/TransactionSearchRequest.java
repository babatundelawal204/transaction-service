package dev.babatunde.transferservice.models.request;

import dev.babatunde.transferservice.constants.TransactionCategory;
import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.exceptions.InvalidDateException;

import java.time.LocalDate;

public record TransactionSearchRequest(String accountNumber,
                                       TransactionStatus status,
                                       LocalDate startDate,
                                       LocalDate endDate,
                                       TransactionCategory category,
                                       TransactionChannel channel,
                                       int page,
                                       int size) {

    public TransactionSearchRequest{
        if(startDate == null && endDate != null){
            throw new InvalidDateException("Provide start date");
        }
    }
}
