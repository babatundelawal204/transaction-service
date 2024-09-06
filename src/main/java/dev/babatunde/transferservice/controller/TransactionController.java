package dev.babatunde.transferservice.controller;

import dev.babatunde.transferservice.constants.TransactionCategory;
import dev.babatunde.transferservice.constants.TransactionChannel;
import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.request.TransactionSearchRequest;
import dev.babatunde.transferservice.models.request.TransferRequest;
import dev.babatunde.transferservice.models.response.TransactionDTO;
import dev.babatunde.transferservice.models.response.TransactionSummaryResponse;
import dev.babatunde.transferservice.models.response.TransferResponse;
import dev.babatunde.transferservice.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public TransferResponse makeTransfer(@RequestBody @Valid TransferRequest transferRequest){
        return transactionService.transferFunds(transferRequest);
    }

    @GetMapping
    public List<TransactionDTO> findAllTransactions(@RequestParam(required = false) String accountNumber,
                                                 @RequestParam(required = false) TransactionStatus status,
                                                 @RequestParam (required = false) LocalDate startDate,
                                                 @RequestParam(required = false) LocalDate endDate,
                                                 @RequestParam(required = false) TransactionCategory category,
                                                 @RequestParam(required = false) TransactionChannel channel,
                                                 @Positive(message = "page must not be less than 1")
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @Positive(message = "size must always be a greater than 0")
                                                 @Max(value = 100, message = "size cannot be greater than 100")
                                                 @RequestParam(defaultValue = "10") int size){
        TransactionSearchRequest transactionSearchRequest =
                new TransactionSearchRequest(accountNumber, status, startDate, endDate, category, channel, page, size);
        return transactionService.findAllTransactions(transactionSearchRequest);
    }

    @GetMapping("/summary")
    public List<TransactionSummaryResponse> generateTransactionSummary(@RequestParam (required = false) LocalDate startDate){
        return transactionService.generateTransactionSummary(startDate);
    }
}
