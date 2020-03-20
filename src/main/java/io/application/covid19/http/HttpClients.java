package io.application.covid19.http;

import io.application.covid19.http.annotations.Covid19HttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClients {

    @Bean
    @Covid19HttpClient
    public RestTemplate restTemplateUzcard(final RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
