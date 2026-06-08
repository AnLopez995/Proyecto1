package com.proyecto1.customerservice.infrastructure.web;

import com.proyecto1.customerservice.domain.exception.ClienteAlreadyExistsException;
import com.proyecto1.customerservice.domain.exception.ClienteNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @Value("${app.errors.include-details:false}")
        private boolean includeDetails;

        @ExceptionHandler(ClienteNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, Object> handleClienteNotFound(
                        ClienteNotFoundException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Cliente no encontrado: {}", exception.getMessage());

                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                request.getRequestURI(),
                                exception);
        }

        @ExceptionHandler(ClienteAlreadyExistsException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String, Object> handleClienteAlreadyExists(
                        ClienteAlreadyExistsException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Cliente duplicado: {}", exception.getMessage());

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                exception.getMessage(),
                                request.getRequestURI(),
                                exception);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String, Object> handleDataIntegrityViolation(
                        DataIntegrityViolationException exception,
                        HttpServletRequest request) {
                LOGGER.error("Error de integridad de datos", exception);

                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                "Error de integridad de datos. Verifica campos únicos u obligatorios.",
                                request.getRequestURI(),
                                exception);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, Object> handleValidationException(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                LOGGER.warn("Error de validación en request: {}", exception.getMessage());

                Map<String, String> validationErrors = new HashMap<>();

                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                        validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                Map<String, Object> response = buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Error de validación",
                                request.getRequestURI(),
                                exception);

                response.put("validationErrors", validationErrors);
                return response;
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String, Object> handleGeneralException(
                        Exception exception,
                        HttpServletRequest request) {
                LOGGER.error("Error inesperado en la petición {}", request.getRequestURI(), exception);

                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error interno del servidor",
                                request.getRequestURI(),
                                exception);
        }

        private Map<String, Object> buildErrorResponse(
                        HttpStatus status,
                        String message,
                        String path,
                        Exception exception) {
                Map<String, Object> response = new HashMap<>();
                response.put("timestamp", LocalDateTime.now());
                response.put("status", status.value());
                response.put("error", status.getReasonPhrase());
                response.put("message", message);
                response.put("path", path);

                if (includeDetails && exception != null) {
                        response.put("exception", exception.getClass().getName());
                        response.put("detail", exception.getMessage());

                        Throwable rootCause = getRootCause(exception);
                        if (rootCause != null && rootCause != exception) {
                                response.put("rootCause", rootCause.getClass().getName());
                                response.put("rootCauseMessage", rootCause.getMessage());
                        }
                }

                return response;
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