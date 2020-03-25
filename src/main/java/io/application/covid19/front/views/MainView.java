package io.application.covid19.front.views;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import io.application.covid19.apis.Covid19Service;
import io.application.covid19.apis.LocationService;
import io.application.covid19.mappers.JsonNodeMapper;
import io.application.covid19.models.CountryInfo;
import io.application.covid19.models.Covid19Record;
import io.application.covid19.models.CovidEntry;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.function.ToLongFunction;

import static com.vaadin.flow.component.charts.model.ChartType.PIE;
import static com.vaadin.flow.component.charts.model.Cursor.POINTER;
import static com.vaadin.flow.component.notification.Notification.show;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static java.lang.String.format;
import static java.text.NumberFormat.getInstance;
import static java.time.LocalDate.now;
import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;
import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Route
@PWA(name = "Covid-19", shortName = "Covid-19")
public class MainView extends VerticalLayout {

    private final Chart pieChart = new Chart(PIE);
    private final Label header = new Label("Covid-19 Statistics");
    private final Label totalCases = new Label();
    private final Label totalRecovered = new Label();
    private final Label totalDeaths = new Label();
    private final Label totalCountries = new Label();
    private final Grid<CovidEntry> grid = new Grid<>(CovidEntry.class, false);
    private DataSeries series;

    public MainView(
            HttpServletRequest request,
            Covid19Service covid19Service,
            LocationService locationService,
            JsonNodeMapper jsonNodeMapper
    ) {

        final List<Covid19Record> records = covid19Service.getCovid19Stat().getRecords();
        final List<CovidEntry> covidEntries = getData(now(), records);

        initChart();
        initLabels(covidEntries);

        grid.setColumnReorderingAllowed(true);
        grid.addColumn(CovidEntry::getCountry).setHeader("Country").setSortable(true);
        grid.addColumn(CovidEntry::getActiveCases).setHeader("Active Cases").setSortable(true);
        grid.addColumn(CovidEntry::getConfirmed).setHeader("Confirmed").setSortable(true);
        grid.addColumn(CovidEntry::getRecovered).setHeader("Recovered").setSortable(true);
        grid.addColumn(CovidEntry::getDeaths).setHeader("Deaths").setSortable(true);
        grid.addColumn(CovidEntry::getDate).setHeader("Date").setSortable(true);
        grid.addItemClickListener((ComponentEventListener<ItemClickEvent<CovidEntry>>) event -> itemClick(event.getItem()));
        grid.setItems(covidEntries);

        final CovidEntry currentEntry = getCurrentCountry(jsonNodeMapper.mapToCountryInfo(locationService.getLocationInfo(request.getRemoteAddr())),
                covidEntries);
        grid.select(currentEntry);
        grid.scrollToIndex(binarySearch(covidEntries, currentEntry, comparing(CovidEntry::getCountry)));
        itemClick(currentEntry);

        final DatePicker datePicker = new DatePicker("Period", now(), event -> updateGrid(event.getValue(), records));

        final HorizontalLayout horizontalLayout = new HorizontalLayout(new VerticalLayout(header, totalCases, totalRecovered, totalDeaths, totalCountries),
                datePicker, pieChart);

        horizontalLayout.setWidthFull();

        setSizeFull();
        setAlignItems(CENTER);
        add(horizontalLayout, grid);
    }

    private static List<CovidEntry> getData(final LocalDate date, final List<Covid19Record> covid19Records) {
        return covid19Records
                .parallelStream()
                .map(covid19Record -> map(covid19Record, date))
                .collect(toList());
    }

    private static Long totalData(final ToLongFunction<CovidEntry> toLongFunction, final List<CovidEntry> records) {
        return records.parallelStream()
                .mapToLong(toLongFunction)
                .sum();
    }

    private static CovidEntry map(final Covid19Record covid19Record, final LocalDate date) {
        final Covid19Record.Data data = covid19Record.getData()
                .stream()
                .filter(recordData -> recordData.getDate().equals(date))
                .findFirst()
                .orElse(covid19Record.getData().get(covid19Record.getData().size() - 1));
        return new CovidEntry(covid19Record.getCountry(), data.getDate(), data.getConfirmed(), data.getDeaths(), data.getRecovered(),
                data.getConfirmed() - (data.getRecovered() + data.getDeaths()));
    }

    private static CovidEntry getCurrentCountry(final CountryInfo countryInfo, final List<CovidEntry> records) {
        return records.parallelStream()
                .filter(record -> record.getCountry().equalsIgnoreCase(countryInfo.getCountry()) ||
                                  record.getCountry().equalsIgnoreCase(countryInfo.getCountryCode()))
                .findFirst().get();
    }

    private void updateGrid(final LocalDate chosenDate, final List<Covid19Record> covidEntries) {
        final List<CovidEntry> entries = getData(chosenDate, covidEntries);
        grid.setItems(entries);
        initLabels(entries);
    }

    private void itemClick(final CovidEntry entry) {
        show(entry.getCountry());
        updateChart(entry);
    }

    private void initChart() {

        //pieChart.setWidth("110%");
        //pieChart.setHeight("110%");

        final Configuration configuration = pieChart.getConfiguration();
        final Tooltip tooltip = new Tooltip();
        configuration.setTooltip(tooltip);

        final PlotOptionsPie plotOptionsPie = new PlotOptionsPie();
        plotOptionsPie.setAllowPointSelect(true);
        plotOptionsPie.setCursor(POINTER);
        plotOptionsPie.setShowInLegend(true);
        configuration.setPlotOptions(plotOptionsPie);

        pieChart.setVisibilityTogglingDisabled(true);
    }

    private void initLabels(final List<CovidEntry> records) {
        header.getStyle().set("font-size", "50px");

        final long cases = totalData(CovidEntry::getConfirmed, records);
        final long recovered = totalData(CovidEntry::getRecovered, records);
        final long deaths = totalData(CovidEntry::getDeaths, records);

        totalCases.setText(format("Total Cases: %s", getInstance(getDefault()).format(cases - (recovered + deaths))));

        totalCases.getStyle().set("font-size", "25px");

        totalRecovered.setText(format("Total Recovered: %s", getInstance(getDefault()).format(recovered)));
        totalRecovered.getStyle().set("font-size", "25px");

        totalDeaths.setText(format("Total Deaths: %s", getInstance(getDefault()).format(deaths)));
        totalDeaths.getStyle().set("font-size", "25px");

        totalCountries.setText(format("Countries with cases: %s", getInstance(getDefault()).format(records.size())));
        totalCountries.getStyle().set("font-size", "25px");
    }

    private void updateChart(final CovidEntry covidEntry) {
        ofNullable(series).ifPresentOrElse(s -> updateSeries(covidEntry), () -> createInitialSeries(covidEntry));
    }

    private void createInitialSeries(final CovidEntry covidEntry) {
        series = new DataSeries();
        final Configuration configuration = pieChart.getConfiguration();

        final DataSeriesItem activeCases = new DataSeriesItem("Active Cases",
                covidEntry.getConfirmed() - (covidEntry.getRecovered() + covidEntry.getDeaths()));
        series.add(activeCases);

        //final DataSeriesItem confirmedSeries = new DataSeriesItem("Confirmed",
        //        covidEntry.getConfirmed());
        ////confirmedSeries.setSliced(true);
        ////confirmedSeries.setSelected(true);
        //series.add(confirmedSeries);

        final DataSeriesItem recoveredSeries = new DataSeriesItem("Recovered",
                covidEntry.getRecovered());
        series.add(recoveredSeries);

        final DataSeriesItem deathSeries = new DataSeriesItem("Deaths",
                covidEntry.getDeaths());
        series.add(deathSeries);
        configuration.setSeries(series);
    }

    private void updateSeries(CovidEntry covidEntry) {
        //series.get("Confirmed").setY(covidEntry.getConfirmed());
        series.get("Recovered").setY(covidEntry.getRecovered());
        series.get("Deaths").setY(covidEntry.getDeaths());
        series.get("Active Cases").setY(covidEntry.getConfirmed() - (covidEntry.getRecovered() + covidEntry.getDeaths()));
        series.updateSeries();
    }
}
