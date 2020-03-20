package io.application.covid19.apis;

import io.application.covid19.impls.integrations.IntegrationType;

public interface Integration<REQUEST, RESPONSE> {

    RESPONSE process(REQUEST request);

    IntegrationType integrationType();

    String description();

    Boolean enabled();

    void enable(final Boolean enable);
}
