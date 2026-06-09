package com.proyecto1.accountservice.infrastructure.messaging;

import com.proyecto1.accountservice.domain.model.ClienteReadModel;
import com.proyecto1.accountservice.infrastructure.persistence.ClienteReadModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClienteEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteEventConsumer.class);

    private static final String CLIENT_CREATED = "CLIENT_CREATED";
    private static final String CLIENT_UPDATED = "CLIENT_UPDATED";
    private static final String CLIENT_DELETED = "CLIENT_DELETED";

    private final ClienteReadModelRepository clienteReadModelRepository;

    public ClienteEventConsumer(ClienteReadModelRepository clienteReadModelRepository) {
        this.clienteReadModelRepository = clienteReadModelRepository;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.CUSTOMER_EVENTS_QUEUE)
    public void consumeClienteEvent(ClienteEvent event) {
        LOGGER.info(
                "Evento de cliente recibido. eventType={}, id={}, codigoCliente={}",
                event.getEventType(),
                event.getId(),
                event.getClienteId());

        if (CLIENT_CREATED.equals(event.getEventType()) || CLIENT_UPDATED.equals(event.getEventType())) {
            upsertClienteReadModel(event);
            return;
        }

        if (CLIENT_DELETED.equals(event.getEventType())) {
            marcarClienteComoInactivo(event);
            return;
        }

        LOGGER.warn("Evento de cliente no reconocido: {}", event.getEventType());
    }

    private void upsertClienteReadModel(ClienteEvent event) {
        ClienteReadModel cliente = clienteReadModelRepository.findById(event.getId())
                .orElseGet(() -> new ClienteReadModel(
                        event.getId(),
                        event.getClienteId(),
                        event.getNombre(),
                        event.getIdentificacion(),
                        event.getEstado()));

        cliente.actualizar(
                event.getClienteId(),
                event.getNombre(),
                event.getIdentificacion(),
                event.getEstado());

        clienteReadModelRepository.save(cliente);

        LOGGER.info(
                "cliente_read_model sincronizado. clienteId={}, codigoCliente={}",
                event.getId(),
                event.getClienteId());
    }

    private void marcarClienteComoInactivo(ClienteEvent event) {
        clienteReadModelRepository.findById(event.getId())
                .ifPresentOrElse(
                        cliente -> {
                            cliente.actualizar(
                                    event.getClienteId(),
                                    event.getNombre(),
                                    event.getIdentificacion(),
                                    false);

                            clienteReadModelRepository.save(cliente);

                            LOGGER.info(
                                    "Cliente marcado como inactivo en cliente_read_model. clienteId={}",
                                    event.getId());
                        },
                        () -> LOGGER.warn(
                                "Se recibió CLIENT_DELETED, pero el cliente no existe en cliente_read_model. clienteId={}",
                                event.getId()));
    }
}