package io.application.covid19.impls.processors;

import io.application.covid19.apis.Integration;
import io.application.covid19.apis.IntegrationFactory;
import io.application.covid19.apis.IntegrationProcessor;
import io.application.covid19.exceptions.IntegrationNotEnabledException;
import io.application.covid19.impls.integrations.IntegrationType;
import io.application.covid19.models.IntegrationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Log4j2
class IntegrationProcessorImpl implements IntegrationProcessor {

    private final IntegrationFactory integrationFactory;

    @Override
    public <REQUEST, RESPONSE> RESPONSE process(IntegrationContext<REQUEST> request) {
        try {
            Integration<REQUEST, RESPONSE> integration = findOne(request.getIntegrationType());
            return integration.process(request.getData());
        } catch (IntegrationNotEnabledException e) {
            log.warn("integration not enabled");
            throw e;
        }
    }

    @Override
    public void enable(IntegrationType integrationType) {
        ofNullable(integrationFactory.fetch(integrationType))
                .ifPresent(integration -> integration.enable(!integration.enabled()));
    }

    @Override
    public Collection<Integration> all() {
        return integrationFactory.all();
    }

    private Integration findOne(final IntegrationType integrationType) {
        return ofNullable(integrationFactory.fetch(integrationType))
                .stream()
                .filter(Integration::enabled)
                .findFirst()
                .orElseThrow(() -> new IntegrationNotEnabledException("", integrationType));
    }
}
