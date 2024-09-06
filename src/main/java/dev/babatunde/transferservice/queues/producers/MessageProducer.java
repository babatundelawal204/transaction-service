package dev.babatunde.transferservice.queues.producers;

public interface MessageProducer {

    <T> void addToQueue(String routingKey, T data);
}