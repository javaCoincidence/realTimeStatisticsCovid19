package io.application.covid19.controllers;

import io.application.covid19.apis.Covid19Service;
import io.application.covid19.models.Covid19;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class Covid19Controller {

    private final Covid19Service covid19Service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Covid19 getStat() {
        return covid19Service.getCovid19Stat();
    }
}
