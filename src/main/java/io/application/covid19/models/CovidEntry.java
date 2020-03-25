package io.application.covid19.models;

import java.time.LocalDate;

public class CovidEntry {

    private final String country;
    private final LocalDate date;
    private final Integer confirmed;
    private final Integer deaths;
    private final Integer recovered;
    private final Integer activeCases;

    public CovidEntry(String country, LocalDate date, Integer confirmed, Integer deaths, Integer recovered, Integer activeCases) {
        this.country = country;
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.activeCases = activeCases;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public Integer getActiveCases() {
        return activeCases;
    }
}