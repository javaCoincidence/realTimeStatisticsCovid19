package io.application.covid19.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.application.covid19.exceptions.IntegrationNotEnabledException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IntegrationNotEnabledException.class)
    @ResponseStatus(FORBIDDEN)
    public JsonNode integrationNotEnabled(final IntegrationNotEnabledException e, final Model model) {
        return new ObjectMapper().createObjectNode().put("message", format("integration %s is not enabled", e.getIntegrationType()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public JsonNode beanValidationException(final ConstraintViolationException e) {
        return new ObjectMapper().createObjectNode().put("message", "invalid integration type");
    }
}
