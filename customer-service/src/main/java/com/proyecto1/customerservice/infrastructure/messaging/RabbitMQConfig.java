package com.proyecto1.customerservice.infrastructure.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }
}
