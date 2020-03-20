package io.application.covid19.impls.factories;

import io.application.covid19.apis.Integration;
import io.application.covid19.apis.IntegrationFactory;
import io.application.covid19.impls.integrations.IntegrationType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
class IntegrationFactoryImpl implements IntegrationFactory {

    private final Map<IntegrationType, Integration> integrationMap;

    public IntegrationFactoryImpl(final List<Integration> integrationList) {
        this.integrationMap = integrationList.stream().collect(toMap(Integration::integrationType, v -> v));
    }

    @Override
    public Integration fetch(IntegrationType integrationType) {
        return integrationMap.get(integrationType);
    }

    @Override
    public Collection<Integration> all() {
        return integrationMap.values();
    }
}
