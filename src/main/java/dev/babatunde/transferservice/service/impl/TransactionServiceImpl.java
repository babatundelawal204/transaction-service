package dev.babatunde.transferservice.service.impl;

import dev.babatunde.transferservice.config.RabbitMqConfig;
import dev.babatunde.transferservice.constants.AccountStatus;
import dev.babatunde.transferservice.constants.TransactionCategory;
import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.constants.TransactionType;
import dev.babatunde.transferservice.exceptions.*;
import dev.babatunde.transferservice.models.db.Account;
import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.models.db.TransactionEntry;
import dev.babatunde.transferservice.models.request.TransactionSearchRequest;
import dev.babatunde.transferservice.models.request.TransferRequest;
import dev.babatunde.transferservice.models.response.TransactionDTO;
import dev.babatunde.transferservice.models.response.TransactionSummaryResponse;
import dev.babatunde.transferservice.models.response.TransferResponse;
import dev.babatunde.transferservice.queues.producers.MessageProducer;
import dev.babatunde.transferservice.repository.JooqTransactionRepository;
import dev.babatunde.transferservice.repository.TransactionEntryRepository;
import dev.babatunde.transferservice.repository.TransactionRepository;
import dev.babatunde.transferservice.service.AccountService;
import dev.babatunde.transferservice.service.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionEntryRepository transactionEntryRepository;

    private final AccountService accountService;

    private final JooqTransactionRepository jooqTransactionRepository;

    private final MessageProducer messageProducer;

    private final BigDecimal transactionsCommissionValue;

    private final BigDecimal transactionsValue;

    private final BigDecimal transactionsAmountCap;


    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionEntryRepository transactionEntryRepository,
                                  AccountService accountService,
                                  JooqTransactionRepository jooqTransactionRepository,
                                  MessageProducer messageProducer,
                                  @Value("${transactions.commissions.value}") BigDecimal transactionsCommissionValue,
                                  @Value("${transactions.value}") BigDecimal transactionsValue,
                                  @Value("${transactions.amount-cap}") BigDecimal transactionsAmountCap) {
        this.transactionRepository = transactionRepository;
        this.transactionEntryRepository = transactionEntryRepository;
        this.accountService = accountService;
        this.jooqTransactionRepository = jooqTransactionRepository;
        this.messageProducer = messageProducer;
        this.transactionsCommissionValue = transactionsCommissionValue;
        this.transactionsValue = transactionsValue;
        this.transactionsAmountCap = transactionsAmountCap;
    }

    @Transactional
    @Override
    public TransferResponse transferFunds(TransferRequest transferRequest) {
        String paymentReference = transferRequest.paymentReference();
        if(transactionRepository.existsByPaymentReference(paymentReference)) {
            throw new TransactionAlreadyExistException(String.format("Transaction with payment reference: %s already exist", paymentReference));
        }
        String currency = transferRequest.currency();
        BigDecimal amount = transferRequest.amount();
        Account senderAccount = accountService.performAccountEnquiry(transferRequest.sourceAccountNumber());
        validateSenderAccount(senderAccount, currency, amount);
        Account recipientAccount = accountService.performAccountEnquiry(transferRequest.destinationAccountNumber());
        validateRecipientAccount(recipientAccount, currency);
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .channel(transferRequest.channel())
                .description(transferRequest.description())
                .category(TransactionCategory.TRANSFERS)
                .account(senderAccount)
                .paymentReference(transferRequest.paymentReference())
                .status(TransactionStatus.PENDING)
                .amount(transferRequest.amount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .statusMessage(TransactionStatus.PENDING.getMessage())
                .currency(transferRequest.currency())
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        List<TransactionEntry> transactionEntries = buildTransaction(savedTransaction, transferRequest, recipientAccount);
        transactionEntryRepository.saveAll(transactionEntries);
        messageProducer.addToQueue(RabbitMqConfig.NEW_TRANSACTIONS_QUEUE, savedTransaction);
        return new TransferResponse(paymentReference);
    }

    private void validateSenderAccount(Account senderAccount, String currency, BigDecimal amount) {
        if (!AccountStatus.ACTIVE.equals(senderAccount.getStatus())) {
            throw new AccountNotActiveException("Sender account is not active");
        }
        if (!senderAccount.getCurrency().equals(currency)) {
            throw new CurrencyMismatchException("Sender account currency mismatch");
        }
        if(senderAccount.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Insufficient funds");
        }
    }

    private void validateRecipientAccount(Account recipientAccount, String currency) {
        if (!recipientAccount.getCurrency().equals(currency)) {
            throw new CurrencyMismatchException("Recipient account currency mismatch");
        }
    }

    private List<TransactionEntry> buildTransaction(Transaction transaction, TransferRequest transferRequest, Account recipientAccount){
        BigDecimal transactionAmount = transferRequest.amount();
        TransactionEntry debitEntry = TransactionEntry.builder()
                .id(UUID.randomUUID())
                .transaction(transaction)
                .account(transaction.getAccount())
                .type(TransactionType.DEBIT)
                .amount(transactionAmount)
                .build();
        TransactionEntry creditEntry = TransactionEntry.builder()
                .id(UUID.randomUUID())
                .transaction(transaction)
                .account(recipientAccount)
                .type(TransactionType.CREDIT)
                .amount(transactionAmount)
                .build();

        return List.of(debitEntry, creditEntry);
    }

    @Transactional
    @Override
    public void completeTransactionProcessing(Transaction transactionMessage) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionMessage.getId());
        if(optionalTransaction.isEmpty())
            return;
        Transaction transaction = optionalTransaction.get();
        List<TransactionEntry> transactionEntries = transaction.getTransactionEntries();
        BigDecimal transactionAmount = transaction.getAmount();
        Account senderAccount = transactionEntries.getFirst().getAccount();
        if(senderAccount.getBalance().compareTo(transaction.getAmount()) < 0 ){
            transaction.setStatus(TransactionStatus.INSUFFICIENT_FUNDS);
        }
        Account recipientAccount = transactionEntries.getFirst().getAccount();
        BigDecimal newBalance = senderAccount.getBalance().subtract(transactionAmount);
        senderAccount.setBalance(newBalance);
        recipientAccount.setBalance(transactionAmount.add(recipientAccount.getBalance()));
        accountService.updateAccounts(senderAccount, recipientAccount);
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        updateTransaction(transaction);
    }

    private void updateTransaction(Transaction transaction){
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> findAllTransactions(TransactionSearchRequest transactionSearchRequest) {
        return jooqTransactionRepository.findAllTransactions(transactionSearchRequest);
    }

    @Override
    public List<Transaction> findTransactionsByStatusForProcessing(TransactionStatus status){
        return transactionRepository.findTop50ByStatusOrderByCreatedAtAsc(status);
    }

    @Override
    public List<TransactionSummaryResponse> generateTransactionSummary(LocalDate date) {
        if(date.isAfter(LocalDate.now())){
            throw new InvalidDateException("date must be either today or older date");
        }
        return jooqTransactionRepository.generateTransactionSummary(date);
    }

    @Override
    public void applyCommissions(Transaction transaction) {
        BigDecimal transactionFee = transaction.getAmount().multiply(transactionsValue.divide(BigDecimal.valueOf(100)));
        if(transactionFee.compareTo(transactionsAmountCap) > 0){
            transactionFee = transactionsAmountCap;
        }
        BigDecimal commissionPercentage = transactionsCommissionValue.divide(BigDecimal.valueOf(100));
        BigDecimal commission = transactionFee.multiply(commissionPercentage);
        transaction.setFee(transactionFee);
        transaction.setCommission(commission);
        transactionRepository.save(transaction);
    }
}
