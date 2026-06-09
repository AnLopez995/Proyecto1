package com.proyecto1.customerservice.infrastructure.web;

import com.proyecto1.customerservice.application.dto.ErrorResponse;
import com.proyecto1.customerservice.domain.exception.ClienteAlreadyExistsException;
import com.proyecto1.customerservice.domain.exception.ClienteNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.amqp.AmqpException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @Value("${app.errors.include-details:false}")
        private boolean includeDetails;

        @ExceptionHandler(ClienteNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleClienteNotFound(
                        ClienteNotFoundException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Cliente no encontrado: {}", exception.getMessage());

                HttpStatus status = HttpStatus.NOT_FOUND;

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                buildDetails(exception));

                return ResponseEntity.status(status).body(response);
        }

        @ExceptionHandler(ClienteAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleClienteAlreadyExists(
                        ClienteAlreadyExistsException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Cliente duplicado: {}", exception.getMessage());

                HttpStatus status = HttpStatus.CONFLICT;

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                buildDetails(exception));

                return ResponseEntity.status(status).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Error de validación en request: {}", exception.getMessage());

                HttpStatus status = HttpStatus.BAD_REQUEST;

                Map<String, Object> validationErrors = new HashMap<>();

                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                        validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "Error de validación",
                                request.getRequestURI(),
                                validationErrors);

                return ResponseEntity.status(status).body(response);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException exception,
                        HttpServletRequest request) {
                LOGGER.error("Error de integridad de datos", exception);

                HttpStatus status = HttpStatus.CONFLICT;

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "Error de integridad de datos. Verifica campos únicos u obligatorios.",
                                request.getRequestURI(),
                                buildDetails(exception));

                return ResponseEntity.status(status).body(response);
        }

        @ExceptionHandler(AmqpException.class)
        public ResponseEntity<ErrorResponse> handleAmqpException(
                        AmqpException exception,
                        HttpServletRequest request) {
                LOGGER.error("Error de comunicación con RabbitMQ", exception);

                HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "No fue posible publicar el evento en RabbitMQ.",
                                request.getRequestURI(),
                                buildDetails(exception));

                return ResponseEntity.status(status).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(
                        Exception exception,
                        HttpServletRequest request) {
                LOGGER.error("Error inesperado en la petición {}", request.getRequestURI(), exception);

                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

                ErrorResponse response = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "Error interno del servidor",
                                request.getRequestURI(),
                                buildDetails(exception));

                return ResponseEntity.status(status).body(response);
        }

        private Map<String, Object> buildDetails(Exception exception) {
                if (!includeDetails || exception == null) {
                        return null;
                }

                Map<String, Object> details = new HashMap<>();
                details.put("exception", exception.getClass().getName());
                details.put("detail", exception.getMessage());

                Throwable rootCause = getRootCause(exception);

                if (rootCause != null && rootCause != exception) {
                        details.put("rootCause", rootCause.getClass().getName());
                        details.put("rootCauseMessage", rootCause.getMessage());
                }

                return details;
        }

        private Throwable getRootCause(Throwable throwable) {
                Throwable cause = throwable.getCause();

                if (cause == null) {
                        return throwable;
                }

                while (cause.getCause() != null) {
                        cause = cause.getCause();
                }

                return cause;
        }
}