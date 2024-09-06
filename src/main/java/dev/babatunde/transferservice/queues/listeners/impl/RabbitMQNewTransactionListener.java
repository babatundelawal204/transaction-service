package dev.babatunde.transferservice.queues.listeners.impl;


import dev.babatunde.transferservice.config.RabbitMqConfig;
import dev.babatunde.transferservice.models.db.Transaction;
import dev.babatunde.transferservice.queues.listeners.MessageListener;
import dev.babatunde.transferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

//This would most likely be in another service, but for demo purpose, we can have it in this same service
@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitMQNewTransactionListener implements MessageListener<Transaction> {

    private final TransactionService transactionService;

    @RabbitListener(queues = RabbitMqConfig.NEW_TRANSACTIONS_QUEUE)
    @Override
    public void handleMessage(Transaction request) {
        transactionService.completeTransactionProcessing(request);
    }
}
