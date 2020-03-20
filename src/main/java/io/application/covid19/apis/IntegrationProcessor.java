package io.application.covid19.apis;

import io.application.covid19.impls.integrations.IntegrationType;
import io.application.covid19.models.IntegrationContext;

import java.util.Collection;

public interface IntegrationProcessor {

    <T, R> R process(final IntegrationContext<T> request);

    void enable(final IntegrationType integrationType);

    Collection<Integration> all();
}
