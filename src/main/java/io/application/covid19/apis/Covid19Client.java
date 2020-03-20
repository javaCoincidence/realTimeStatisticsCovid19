package io.application.covid19.apis;

import io.application.covid19.models.Covid19;

public interface Covid19Client {

    Covid19 fetchStat();
}
