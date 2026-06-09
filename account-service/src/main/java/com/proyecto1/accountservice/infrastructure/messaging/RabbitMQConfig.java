package com.proyecto1.accountservice.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";
    public static final String CUSTOMER_EVENTS_QUEUE = "account.customer.events.queue";

    public static final String CUSTOMER_CREATED_ROUTING_KEY = "customer.created";
    public static final String CUSTOMER_UPDATED_ROUTING_KEY = "customer.updated";
    public static final String CUSTOMER_DELETED_ROUTING_KEY = "customer.deleted";

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE, true, false);
    }

    @Bean
    public Queue customerEventsQueue() {
        return new Queue(CUSTOMER_EVENTS_QUEUE, true);
    }

    @Bean
    public Binding customerCreatedBinding(
            Queue customerEventsQueue,
            TopicExchange customerExchange) {
        return BindingBuilder
                .bind(customerEventsQueue)
                .to(customerExchange)
                .with(CUSTOMER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding customerUpdatedBinding(
            Queue customerEventsQueue,
            TopicExchange customerExchange) {
        return BindingBuilder
                .bind(customerEventsQueue)
                .to(customerExchange)
                .with(CUSTOMER_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding customerDeletedBinding(
            Queue customerEventsQueue,
            TopicExchange customerExchange) {
        return BindingBuilder
                .bind(customerEventsQueue)
                .to(customerExchange)
                .with(CUSTOMER_DELETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");

        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }
}
