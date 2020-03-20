package io.application.covid19.models;

import io.application.covid19.impls.integrations.IntegrationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class IntegrationContext<T> {

    private final T data;
    private final IntegrationType integrationType;
}
