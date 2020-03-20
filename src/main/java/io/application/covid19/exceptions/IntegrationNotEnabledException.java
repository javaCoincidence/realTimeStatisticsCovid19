package io.application.covid19.exceptions;

import io.application.covid19.impls.integrations.IntegrationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class IntegrationNotEnabledException extends RuntimeException {

    private IntegrationType integrationType;

    public IntegrationNotEnabledException(String message, IntegrationType integrationType) {
        super(message);
        this.integrationType = integrationType;
    }

    public IntegrationNotEnabledException() {
    }
}
