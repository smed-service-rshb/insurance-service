package ru.softlab.efr.services.insurance.stubs;

import ru.softlab.efr.common.client.CurrencyRateClient;
import ru.softlab.efr.common.dict.exchange.model.CurrencyCbRateData;
import ru.softlab.efr.common.dict.exchange.model.CurrencyCbRateRs;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CurrencyRateClientStub extends CurrencyRateClient {
    public CurrencyRateClientStub(MicroServiceTemplate microServiceTemplate) {
        super(microServiceTemplate);
    }

    @Override
    public CurrencyCbRateRs getCbCurrencyRateList(String literalIso, String startDate, String endDate, long timeout) throws RestClientException {
        CurrencyCbRateRs rs = new CurrencyCbRateRs();
        int i = 1;
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        while (start.isBefore(end)){
            rs.addRatesItem(new CurrencyCbRateData(literalIso, start.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), BigDecimal.valueOf(i++)));
            start = start.plusDays(1);
        }
        return rs;
    }
}
