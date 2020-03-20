package io.application.covid19.impls.integrations;

import io.application.covid19.apis.Covid19Client;
import io.application.covid19.apis.Integration;
import io.application.covid19.models.Covid19;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static io.application.covid19.impls.integrations.IntegrationType.F19D;

@Component
@RequiredArgsConstructor
class Covid19StatIntegration implements Integration<Void, Covid19> {

    private final Covid19Client covid19Client;
    private Boolean enabled = true;

    @SneakyThrows
    @Override
    public Covid19 process(Void request) {
        return covid19Client.fetchStat();
    }

    @Override
    public IntegrationType integrationType() {
        return F19D;
    }

    @Override
    public String description() {
        return "Fetches covid19 data";
    }

    @Override
    public synchronized Boolean enabled() {
        return enabled;
    }

    @Override
    public synchronized void enable(Boolean enable) {
        this.enabled = enable;
    }
}
