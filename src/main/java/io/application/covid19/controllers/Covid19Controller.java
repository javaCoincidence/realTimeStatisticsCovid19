package io.application.covid19.controllers;

import io.application.covid19.apis.Covid19Service;
import io.application.covid19.models.Covid19;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
public class Covid19Controller {

    private final Covid19Service covid19Service;

    @GetMapping
    public String getStat(final Model model) {
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
