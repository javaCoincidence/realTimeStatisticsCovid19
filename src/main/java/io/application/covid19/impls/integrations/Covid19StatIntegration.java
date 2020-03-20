package io.application.covid19.impls.integrations;

import io.application.covid19.apis.Covid19Client;
import io.application.covid19.apis.Integration;
import io.application.covid19.models.Covid19;
import io.application.covid19.models.Covid19Record;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.StringReader;

import static io.application.covid19.impls.integrations.IntegrationType.F19D;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.csv.CSVFormat.DEFAULT;

@Component
@RequiredArgsConstructor
class Covid19StatIntegration implements Integration<Void, Covid19> {

    private final Covid19Client covid19Client;
    private Boolean enabled = true;

    private static Covid19Record parse(final CSVRecord record) {
        final var latestCases = Integer.parseInt(record.get(record.size() - 1));
        final var diffFromLastDay = latestCases - Integer.parseInt(record.get(record.size() - 2));
        return new Covid19Record(record.get("Country/Region"), latestCases, diffFromLastDay);
    }

    @SneakyThrows
    @Override
    public Covid19 process(Void request) {
        try (final CSVParser csvParser = DEFAULT.withFirstRecordAsHeader().parse(new StringReader(covid19Client.fetchStat()))) {
            return new Covid19(stream(csvParser.spliterator(), false)
                    .map(Covid19StatIntegration::parse)
                    .collect(toList()));
        }
    }

    @Override
    public IntegrationType integrationType() {
        return F19D;
    }

    @Override
    public String description() {
        return "Fetches covid19 data";
    }

    @Override
    public Boolean enabled() {
        return enabled;
    }

    @Override
    public void enable(Boolean enable) {
        this.enabled = enable;
    }
}
