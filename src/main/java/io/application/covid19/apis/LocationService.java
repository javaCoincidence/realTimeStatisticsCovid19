package io.application.covid19.apis;

import com.fasterxml.jackson.databind.JsonNode;

public interface LocationService {

    JsonNode getLocationInfo(final String ip);
}
