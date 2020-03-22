package io.application.covid19.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.application.covid19.apis.Covid19Service;
import io.application.covid19.apis.LocationService;
import io.application.covid19.models.Covid19Record;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static java.util.stream.Collectors.toList;

@Route
public class MainView extends VerticalLayout {

    private final Covid19Service covid19Service;
    private final LocationService locationService;

    public MainView(HttpServletRequest request, Covid19Service covid19Service, LocationService locationService) {
        this.covid19Service = covid19Service;
        this.locationService = locationService;
        init(request.getRemoteAddr());
    }

    private static Long currentCountryTotalCases(final String countryName, final List<CovidEntry> records) {
        return records.stream()
                .filter(record -> record.getCountry().equalsIgnoreCase(countryName))
                .mapToLong(CovidEntry::getConfirmed)
                .sum();
    }

    private static CovidEntry map(final Covid19Record covid19Record) {
        return new CovidEntry(covid19Record.getCountry(), covid19Record.getData().get(covid19Record.getData().size() - 1).getDate(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getConfirmed(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getDeaths(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getRecovered());
    }

    public void init(final String ip) {
        final List<CovidEntry> records = covid19Service.getCovid19Stat()
                .getRecords()
                .stream()
                .map(MainView::map)
                .collect(toList());

        final Label header = new Label("Covid19 Statistics");
        header.getStyle().set("font-size", "50px");

        final Label totalCases = new Label(String.format("Total Cases: %s", records.stream()
                .mapToLong(CovidEntry::getConfirmed)
                .sum()));
        totalCases.getStyle().set("font-size", "25px");

        final String currentCountryName = locationService.getLocationInfo("213.230.95.59").get("country_name").asText();
        final Label currentInfoLabel = new Label(String.format("%s total confirmed cases: %s", currentCountryName,
                currentCountryTotalCases(currentCountryName, records)));
        currentInfoLabel.getStyle().set("font-size", "20px");

        final Grid<CovidEntry> grid = new Grid<>(CovidEntry.class, false);

        grid.addColumn(CovidEntry::getCountry).setHeader("Country");
        grid.addColumn(CovidEntry::getDate).setHeader("Date");
        grid.addColumn(CovidEntry::getConfirmed).setHeader("Confirmed");
        grid.addColumn(CovidEntry::getRecovered).setHeader("Recovered");
        grid.addColumn(CovidEntry::getDeaths).setHeader("Deaths");

        grid.setItems(records);

        setSizeFull();
        setAlignItems(CENTER);
        add(header, totalCases, currentInfoLabel, grid);
    }

    public static class CovidEntry {

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
}
