package io.application.covid19.front.views;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsPie;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.application.covid19.apis.Covid19Service;
import io.application.covid19.apis.LocationService;
import io.application.covid19.mappers.JsonNodeMapper;
import io.application.covid19.models.CountryInfo;
import io.application.covid19.models.Covid19Record;
import io.application.covid19.models.CovidEntry;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.function.ToLongFunction;

import static com.vaadin.flow.component.charts.model.ChartType.PIE;
import static com.vaadin.flow.component.charts.model.Cursor.POINTER;
import static com.vaadin.flow.component.notification.Notification.show;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static java.text.NumberFormat.getInstance;
import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;
import static java.util.Locale.getDefault;
import static java.util.stream.Collectors.toList;

@Route
public class MainView extends VerticalLayout {

    private final Chart pieChart = new Chart(PIE);
    private final Label header = new Label("Covid-19 Statistics");
    private final Label totalCases = new Label();
    private final Label totalRecovered = new Label();
    private final Label totalDeaths = new Label();
    private final Grid<CovidEntry> grid = new Grid<>(CovidEntry.class, false);
    private DataSeries series;

    public MainView(
            HttpServletRequest request,
            Covid19Service covid19Service,
            LocationService locationService,
            JsonNodeMapper jsonNodeMapper
    ) {
        final List<CovidEntry> records = getData(covid19Service);

        initChart();
        initLabels(records);

        grid.setColumnReorderingAllowed(true);
        grid.addColumn(CovidEntry::getCountry).setHeader("Country").setSortable(true);
        grid.addColumn(CovidEntry::getDate).setHeader("Date").setSortable(true);
        grid.addColumn(CovidEntry::getConfirmed).setHeader("Confirmed").setSortable(true);
        grid.addColumn(CovidEntry::getRecovered).setHeader("Recovered").setSortable(true);
        grid.addColumn(CovidEntry::getDeaths).setHeader("Deaths").setSortable(true);
        grid.addItemClickListener((ComponentEventListener<ItemClickEvent<CovidEntry>>) event -> itemClick(event.getItem()));
        grid.setItems(records);

        final CovidEntry currentEntry = getCurrentCountry(jsonNodeMapper.mapToCountryInfo(locationService.getLocationInfo(request.getRemoteAddr())), records);
        grid.select(currentEntry);
        grid.scrollToIndex(binarySearch(records, currentEntry, comparing(CovidEntry::getCountry)));
        itemClick(currentEntry);

        final HorizontalLayout horizontalLayout = new HorizontalLayout(new VerticalLayout(header, totalCases, totalRecovered, totalDeaths), pieChart);
        horizontalLayout.setWidthFull();

        setSizeFull();
        setAlignItems(CENTER);
        add(horizontalLayout, grid);
    }

    private static List<CovidEntry> getData(final Covid19Service covid19Service) {
        return covid19Service.getCovid19Stat()
                .getRecords()
                .parallelStream()
                .map(MainView::map)
                .collect(toList());
    }

    private static Long totalData(final ToLongFunction<CovidEntry> toLongFunction, final List<CovidEntry> records) {
        return records.parallelStream()
                .mapToLong(toLongFunction)
                .sum();
    }

    private static CovidEntry map(final Covid19Record covid19Record) {
        return new CovidEntry(covid19Record.getCountry(), covid19Record.getData().get(covid19Record.getData().size() - 1).getDate(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getConfirmed(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getDeaths(),
                covid19Record.getData().get(covid19Record.getData().size() - 1).getRecovered());
    }

    private static CovidEntry getCurrentCountry(final CountryInfo countryInfo, final List<CovidEntry> records) {
        return records.parallelStream()
                .filter(record -> record.getCountry().equalsIgnoreCase(countryInfo.getCountry()) || record.getCountry().equalsIgnoreCase(countryInfo.getCountryCode()))
                .findFirst().get();
    }

    private void itemClick(final CovidEntry entry) {
        show(entry.getCountry());
        updateChart(entry);
    }

    private void initChart() {

        pieChart.setWidth("115%");
        pieChart.setHeight("115%");

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

        totalCases.setText(String.format("Total Cases: %s", getInstance(getDefault()).format(totalData(CovidEntry::getConfirmed, records))));

        totalCases.getStyle().set("font-size", "25px");

        totalRecovered.setText(String.format("Total Recovered: %s", getInstance(getDefault()).format(totalData(CovidEntry::getRecovered, records))));
        totalRecovered.getStyle().set("font-size", "25px");

        totalDeaths.setText(String.format("Total Deaths: %s", getInstance(getDefault()).format(totalData(CovidEntry::getDeaths, records))));
        totalDeaths.getStyle().set("font-size", "25px");
    }

    private void updateChart(final CovidEntry covidEntry) {
        if (series == null) {
            series = new DataSeries();
            final Configuration configuration = pieChart.getConfiguration();

            final DataSeriesItem confirmedSeries = new DataSeriesItem("Total Confirmed",
                    covidEntry.getConfirmed());
            confirmedSeries.setSliced(true);
            confirmedSeries.setSelected(true);
            series.add(confirmedSeries);

            final DataSeriesItem recoveredSeries = new DataSeriesItem("Total Recovered",
                    covidEntry.getRecovered());
            series.add(recoveredSeries);

            final DataSeriesItem deathSeries = new DataSeriesItem("Total Deaths",
                    covidEntry.getDeaths());
            series.add(deathSeries);
            configuration.setSeries(series);
        } else {
            series.get("Total Confirmed").setY(covidEntry.getConfirmed());
            series.get("Total Recovered").setY(covidEntry.getRecovered());
            series.get("Total Deaths").setY(covidEntry.getDeaths());
            series.updateSeries();
        }
    }
}
