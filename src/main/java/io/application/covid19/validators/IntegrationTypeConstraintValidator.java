package io.application.covid19.validators;

import io.application.covid19.impls.integrations.IntegrationType;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.stream;

@Component
public class IntegrationTypeConstraintValidator implements ConstraintValidator<ValidIntegrationType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return stream(IntegrationType.values())
                .anyMatch(integrationType -> integrationType.name().equals(value));
    }
}
