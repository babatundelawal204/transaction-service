package dev.babatunde.transferservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String NEW_TRANSACTIONS_QUEUE = "transactions.new";
    public static final String TRANSACTION_COMPLETED_QUEUE = "transactions.completed";
    public static final String TOPIC_EXCHANGE_NAME = "exchange";
    public static final String NEW_TRANSACTIONS_ROUTING_KEY = "transactions.new";
    public static final String TRANSACTIONS_COMPLETED_ROUTING_KEY = "transactions.completed";
    public static final String TRANSACTION_SUMMARY_QUEUE = "transactions.summary";
    public static final String TRANSACTION_SUMMARY_KEY = "transactions.new";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue newTransactionQueue() {
        return new Queue(NEW_TRANSACTIONS_QUEUE);
    }

    @Bean
    public Queue completedTransactionQueue() {
        return new Queue(TRANSACTION_COMPLETED_QUEUE);
    }

    @Bean
    public Queue transactionSummaryQueue() {
        return new Queue(TRANSACTION_SUMMARY_QUEUE);
    }

    @Bean
    Binding bindingNewTransactionQueue(TopicExchange exchange) {
        return BindingBuilder.bind(newTransactionQueue())
                .to(exchange)
                .with(NEW_TRANSACTIONS_ROUTING_KEY);
    }

    @Bean
    Binding bindingTransactionCompletedQueue(TopicExchange exchange) {
        return BindingBuilder.bind(completedTransactionQueue())
                .to(exchange)
                .with(TRANSACTIONS_COMPLETED_ROUTING_KEY);
    }

    @Bean
    Binding bindingTransactionSummaryQueue(TopicExchange exchange) {
        return BindingBuilder.bind(transactionSummaryQueue())
                .to(exchange)
                .with(TRANSACTION_SUMMARY_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory,
                                     MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


}