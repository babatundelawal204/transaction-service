package dev.babatunde.transferservice.queues.producers;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionQueue implements MessageProducer{

    private final AmqpTemplate amqpTemplate;

    @Override
    public <T> void addToQueue(String queue, T data) {
        amqpTemplate.convertAndSend(queue, data);
    }
}
