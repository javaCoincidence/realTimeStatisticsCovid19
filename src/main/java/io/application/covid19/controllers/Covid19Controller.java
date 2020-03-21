package io.application.covid19.controllers;

import io.application.covid19.apis.Covid19Service;
import io.application.covid19.models.Covid19;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@Controller
@RequiredArgsConstructor
public class Covid19Controller {

    private final Covid19Service covid19Service;

    private String test;

    @GetMapping
    public String getStat(final Model model) {
        log.info(test);
        final Covid19 covid19 = getStat();
        model.addAttribute("stat", covid19.getRecords());
        return "index";
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Covid19 getStat() {
        return covid19Service.getCovid19Stat();
    }
}
