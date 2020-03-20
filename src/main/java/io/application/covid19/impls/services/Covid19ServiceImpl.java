package io.application.covid19.impls.services;

import io.application.covid19.apis.Covid19Service;
import io.application.covid19.apis.IntegrationProcessor;
import io.application.covid19.models.Covid19;
import io.application.covid19.models.IntegrationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.application.covid19.impls.integrations.IntegrationType.F19D;

@Service
@RequiredArgsConstructor
class Covid19ServiceImpl implements Covid19Service {

    private final IntegrationProcessor integrationProcessor;

    @Override
    public Covid19 getCovid19Stat() {
        return integrationProcessor.process(new IntegrationContext<>(null, F19D));
    }
}
