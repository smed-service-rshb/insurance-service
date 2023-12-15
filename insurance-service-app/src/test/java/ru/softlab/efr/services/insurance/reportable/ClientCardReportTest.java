package ru.softlab.efr.services.insurance.reportable;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClientCardReportTest {

    @Test
    public void getModifiedData() {
        List<LocalDate> clientCheck = new ArrayList<>();
        clientCheck.add(LocalDate.parse("2019-01-29"));
        clientCheck.add(LocalDate.parse("2017-12-29"));
        clientCheck.add(LocalDate.parse("2016-01-31"));
        clientCheck.add(LocalDate.parse("2018-04-30"));
        List<LocalDate> dates = ClientCardReport.getModifiedDate(clientCheck, LocalDate.parse("2015-06-25"));
        assertEquals(LocalDate.parse("2016-01-31"), dates.get(0));
        assertEquals(LocalDate.parse("2017-12-29"), dates.get(1));
        assertEquals(LocalDate.parse("2018-04-30"), dates.get(2));
    }
}