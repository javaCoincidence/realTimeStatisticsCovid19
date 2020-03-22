package io.application.covid19.impls.clients;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.apis.LocationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class LocationClientImpl implements LocationClient {

    private final RestTemplate restTemplate;

    @Override
    public JsonNode info(final String ip) {
        return restTemplate.getForObject(format("https://ipapi.co/%s/json/", ip), JsonNode.class);
    }
}
