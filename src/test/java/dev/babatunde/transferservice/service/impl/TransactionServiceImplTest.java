package dev.babatunde.transferservice.service.impl;

import dev.babatunde.transferservice.repository.JooqTransactionRepository;
import dev.babatunde.transferservice.repository.TransactionEntryRepository;
import dev.babatunde.transferservice.repository.TransactionRepository;
import dev.babatunde.transferservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionEntryRepository transactionEntryRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private JooqTransactionRepository jooqTransactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void transferFunds_whenPaymentReferenceAlreadyExists_throwAlreadyExistException() {

    }

    @Test
    void completeTransactionProcessing() {
    }

    @Test
    void findAllTransactions() {
    }

    @Test
    void findTransactionsByStatusForProcessing() {
    }

    @Test
    void generateTransactionSummary() {
    }

    @Test
    void applyCommissions() {
    }
}