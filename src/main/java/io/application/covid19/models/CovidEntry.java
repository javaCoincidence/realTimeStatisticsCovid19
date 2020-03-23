package io.application.covid19.models;

public class CovidEntry {

    private final String country;
    private final String date;
    private final Integer confirmed;
    private final Integer deaths;
    private final Integer recovered;

    public CovidEntry(String country, String date, Integer confirmed, Integer deaths, Integer recovered) {
        this.country = country;
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
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
}