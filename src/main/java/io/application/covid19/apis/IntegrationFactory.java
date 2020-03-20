package io.application.covid19.apis;

import io.application.covid19.impls.integrations.IntegrationType;

import java.util.Collection;

public interface IntegrationFactory {

    Integration fetch(final IntegrationType integrationType);

    Collection<Integration> all();
}
