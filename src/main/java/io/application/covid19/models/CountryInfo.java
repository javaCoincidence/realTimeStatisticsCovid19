package io.application.covid19.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class CountryInfo {

    private final String country;
    private final String countryCode;
}
