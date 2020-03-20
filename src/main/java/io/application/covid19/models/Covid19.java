package io.application.covid19.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class Covid19 {

    private final List<Covid19Record> records;
    private Integer totalCases;

    public Integer getTotalCases() {
        return records.stream().mapToInt(Covid19Record::getLatestCases).sum();
    }

    public Integer getTotalNewCases() {
        return records.stream().mapToInt(Covid19Record::getDiffFromLastDay).sum();
    }
}
