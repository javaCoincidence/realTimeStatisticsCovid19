package io.application.covid19.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.apis.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationInfoController {

    private final LocationService locationService;

    @GetMapping
    public JsonNode getLocation(final HttpServletRequest request) {
        return locationService.getLocationInfo(request.getRemoteAddr());
    }
}
