package io.application.covid19.impls.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.apis.Integration;
import io.application.covid19.apis.LocationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.application.covid19.impls.integrations.IntegrationType.IPFC;

@Component
@RequiredArgsConstructor
public class LocationInfoIntegration implements Integration<String, JsonNode> {

    private final LocationClient locationClient;
    private Boolean enabled = true;

    @Override
    public JsonNode process(String request) {
        return locationClient.info(request);
    }

    @Override
    public IntegrationType integrationType() {
        return IPFC;
    }

    @Override
    public String description() {
        return "Fetches Country info from Ip";
    }

    @Override
    public Boolean enabled() {
        return enabled;
    }

    @Override
    public void enable(Boolean enable) {
        this.enabled = enable;
    }
}
