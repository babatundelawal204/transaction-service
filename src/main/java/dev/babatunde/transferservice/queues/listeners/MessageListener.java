package dev.babatunde.transferservice.queues.listeners;

public interface MessageListener<T> {

    void handleMessage(T request);
}
