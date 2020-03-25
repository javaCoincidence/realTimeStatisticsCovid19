package io.application.covid19.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class Covid19Record {

    private final String country;
    private final List<Data> data;

    @Setter
    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class Data {

        private final LocalDate date;
        private final Integer confirmed;
        private final Integer deaths;
        private final Integer recovered;
    }
}
