package io.application.covid19.impls.clients;

import io.application.covid19.apis.Covid19Client;
import io.application.covid19.http.annotations.Covid19HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class Covid19ClientImpl implements Covid19Client {

    private final RestTemplate restTemplate;

    @Value("${covid19-url}")
    private String url;

    public Covid19ClientImpl(@Covid19HttpClient RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String fetchStat() {
        return restTemplate.getForEntity(url, String.class).getBody();
    }
}
