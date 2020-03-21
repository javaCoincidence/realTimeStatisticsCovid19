package io.application.covid19.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import io.application.covid19.models.Covid19;
import io.application.covid19.models.Covid19Record;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Mapper(componentModel = "spring")
public interface JsonNodeMapper {

    @SneakyThrows
    static Covid19Record covid19Object(final String fieldName, final JsonNode node) {
        return new Covid19Record(fieldName, data(node));
    }

    static List<Covid19Record.Data> data(final JsonNode node) {
        return stream(node.spliterator(), true)
                .map(data -> new Covid19Record.Data(data.get("date").asText(), data.get("confirmed").asInt(),
                        data.get("deaths").asInt(), data.get("recovered").asInt()))
                .collect(toList());
    }

    default Covid19 map(final JsonNode root) {
        final List<Covid19Record> list = new ArrayList<>();
        final Iterator<Map.Entry<String, JsonNode>> it = root.fields();
        while (it.hasNext()) {
            final Map.Entry<String, JsonNode> entry = it.next();
            list.add(covid19Object(entry.getKey(), entry.getValue()));
        }
        list.sort(comparing(Covid19Record::getCountry));
        return new Covid19(list);
    }
}
