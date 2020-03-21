package io.application.covid19.impls.clients;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.apis.Covid19Client;
import io.application.covid19.http.annotations.Covid19HttpClient;
import io.application.covid19.mappers.JsonNodeMapper;
import io.application.covid19.models.Covid19;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;

@Component
class Covid19ClientImpl implements Covid19Client {

    private final RestTemplate restTemplate;
    private final JsonNodeMapper jsonNodeMapper;

    @Value("${covid19-url}")
    private String url;

    public Covid19ClientImpl(@Covid19HttpClient RestTemplate restTemplate, JsonNodeMapper jsonNodeMapper) {
        this.restTemplate = restTemplate;
        this.jsonNodeMapper = jsonNodeMapper;
    }

    @Override
    public Covid19 fetchStat() {
        return jsonNodeMapper.map(requireNonNull(restTemplate.getForEntity(url, JsonNode.class).getBody()));
    }
}
