package io.application.covid19.apis;

import com.fasterxml.jackson.databind.JsonNode;

public interface LocationClient {

    JsonNode info(final String ip);
}
