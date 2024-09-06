package dev.babatunde.transferservice.jobs;

import dev.babatunde.transferservice.config.RabbitMqConfig;
import dev.babatunde.transferservice.models.response.TransactionSummaryResponse;
import dev.babatunde.transferservice.queues.producers.MessageProducer;
import dev.babatunde.transferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class TransactionSummaryJob {

    private final TransactionService transactionService;

    private final MessageProducer messageProducer;


    @Scheduled(cron = "${transactions.summary.cron}")
    public void generateTransactionSummary(){
        List<TransactionSummaryResponse> transactionSummaryForTheDay = transactionService.generateTransactionSummary(LocalDate.now());
        log.info("Generating transaction summary: {}", transactionSummaryForTheDay);
        messageProducer.addToQueue(RabbitMqConfig.TRANSACTION_SUMMARY_QUEUE, transactionSummaryForTheDay);
    }
}
