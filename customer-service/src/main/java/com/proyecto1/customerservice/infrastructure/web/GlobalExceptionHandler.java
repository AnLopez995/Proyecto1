package com.proyecto1.customerservice.infrastructure.web;

import com.proyecto1.customerservice.domain.exception.ClienteAlreadyExistsException;
import com.proyecto1.customerservice.domain.exception.ClienteNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ClienteNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, Object> handleClienteNotFound(
                        ClienteNotFoundException exception,
                        HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                request.getRequestURI());
        }

        @ExceptionHandler(ClienteAlreadyExistsException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String, Object> handleClienteAlreadyExists(
                        ClienteAlreadyExistsException exception,
                        HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                exception.getMessage(),
                                request.getRequestURI());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, Object> handleValidationException(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                Map<String, String> validationErrors = new HashMap<>();

                for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                        validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                Map<String, Object> response = buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Error de validación",
                                request.getRequestURI());

                response.put("validationErrors", validationErrors);
                return response;
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Map<String, Object> handleGeneralException(
                        Exception exception,
                        HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error interno del servidor",
                                request.getRequestURI());
        }

        private Map<String, Object> buildErrorResponse(HttpStatus status, String message, String path) {
                Map<String, Object> response = new HashMap<>();
                response.put("timestamp", LocalDateTime.now());
                response.put("status", status.value());
                response.put("error", status.getReasonPhrase());
                response.put("message", message);
                response.put("path", path);
                return response;
        }
}