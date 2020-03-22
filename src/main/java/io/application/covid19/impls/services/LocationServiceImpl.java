package io.application.covid19.impls.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.apis.IntegrationProcessor;
import io.application.covid19.apis.LocationService;
import io.application.covid19.models.IntegrationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.application.covid19.impls.integrations.IntegrationType.IPFC;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final IntegrationProcessor integrationProcessor;

    @Override
    public JsonNode getLocationInfo(String ip) {
        return integrationProcessor.process(new IntegrationContext<>(ip, IPFC));
    }
}
