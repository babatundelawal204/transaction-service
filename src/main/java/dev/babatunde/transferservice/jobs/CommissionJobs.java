package dev.babatunde.transferservice.jobs;

import dev.babatunde.transferservice.constants.TransactionStatus;
import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommissionJobs {

    @Value("${transactions.commissions.cron}")
    private String transactionCommissionsCron;

    private final TransactionService transactionService;

    private final TransactionTemplate transactionTemplate;


    // Use the injected cron expression for scheduling
    @Scheduled(cron = "${transactions.commissions.cron}")
    public void runCommissionJobs(){
        boolean isPending = true;
        while(isPending){
            isPending =  transactionTemplate.execute(status -> {
                List<Transaction> transactions = transactionService.findTransactionsByStatusForProcessing(TransactionStatus.SUCCESSFUL);
                if(transactions.isEmpty()){
                    return false;
                }
                transactions.forEach(transactionService::applyCommissions);
                return true;
            });
        }



    }

}
