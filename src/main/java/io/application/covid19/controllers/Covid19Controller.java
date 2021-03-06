package io.application.covid19.controllers;

import io.application.covid19.apis.Covid19Service;
import io.application.covid19.models.Covid19;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequestMapping("/raw")
@RequiredArgsConstructor
public class Covid19Controller {

    private final Covid19Service covid19Service;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Covid19 getStat(final HttpServletRequest request) {
        return covid19Service.getCovid19Stat();
    }
}
