package com.proyecto1.customerservice.infrastructure.messaging;

import com.proyecto1.customerservice.domain.model.Cliente;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ClienteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClienteCreated(Cliente cliente) {
        ClienteEvent event = buildEvent("CLIENT_CREATED", cliente);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                "customer.created",
                event);
    }

    public void publishClienteUpdated(Cliente cliente) {
        ClienteEvent event = buildEvent("CLIENT_UPDATED", cliente);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                "customer.updated",
                event);
    }

    public void publishClienteDeleted(Cliente cliente) {
        ClienteEvent event = buildEvent("CLIENT_DELETED", cliente);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                "customer.deleted",
                event);
    }

    private ClienteEvent buildEvent(String eventType, Cliente cliente) {
        return new ClienteEvent(
                eventType,
                cliente.getId(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado());
    }
}