package io.application.covid19.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.application.covid19.apis.IntegrationProcessor;
import io.application.covid19.impls.integrations.IntegrationType;
import io.application.covid19.validators.ValidIntegrationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/integrations", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class IntegrationController {

    private final IntegrationProcessor integrationProcessor;

    @PutMapping("enable/{type}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void enable(@PathVariable("type") @ValidIntegrationType final String integrationType) {
        integrationProcessor.enable(IntegrationType.valueOf(integrationType));
    }

    @GetMapping
    public List<JsonNode> all() {
        return integrationProcessor.all().stream()
                .map(integration -> new ObjectMapper().createObjectNode()
                        .put("integrationType", integration.integrationType().toString())
                        .put("description", integration.description())
                        .put("enabled", integration.enabled()))
                .collect(toList());
    }
}
