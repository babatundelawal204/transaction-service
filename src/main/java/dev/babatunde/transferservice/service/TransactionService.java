package dev.babatunde.transferservice.service;

import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.models.request.TransactionSearchRequest;
import dev.babatunde.transferservice.models.request.TransferRequest;
import dev.babatunde.transferservice.models.response.TransactionDTO;
import dev.babatunde.transferservice.models.response.TransactionSummaryResponse;
import dev.babatunde.transferservice.models.response.TransferResponse;
import dev.babatunde.transferservice.queues.messages.TransactionMessage;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransferResponse transferFunds(TransferRequest transferRequest);

    List<TransactionDTO> findAllTransactions(TransactionSearchRequest transactionSearchRequest);

    List<TransactionSummaryResponse> generateTransactionSummary(LocalDate date);

    void completeTransactionProcessing(Transaction request);

    List<Transaction> findTransactionsByStatusForProcessing(TransactionStatus staus);

    void applyCommissions(Transaction transaction);
}
