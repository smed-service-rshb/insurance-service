package ru.softlab.efr.services.insurance.services;

import org.junit.Test;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.ReportableRedemption;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Тесты для проверки расчётов выкупных сумм.
 *
 * @author olshansky
 * @since 05.12.2018
 */
public class BuyoutServiceTest {

    /**
     * Срок страхования - 5 лет.
     * Выплаты - ежегодно.
     */
    @Test
    public void shouldBe5Periods() {
        shouldBeNumberPeriods(5, CalendarUnitEnum.YEAR, PeriodicityEnum.YEARLY, 5);
    }

    /**
     * Срок страхования - 5 лет.
     * Выплаты - раз в полгода.
     */
    @Test
    public void shouldBe10Periods() {
        shouldBeNumberPeriods(5, CalendarUnitEnum.YEAR, PeriodicityEnum.TWICE_A_YEAR, 10);
    }

    /**
     * Срок страхования - 5 лет.
     * Выплаты - ежеквартально.
     */
    @Test
    public void shouldBe20Periods() {
        shouldBeNumberPeriods(5, CalendarUnitEnum.YEAR, PeriodicityEnum.QUARTERLY, 20);
    }

    /**
     * Срок страхования - 5 лет.
     * Выплаты - ежемесячно.
     */
    @Test
    public void shouldBe60Periods() {
        shouldBeNumberPeriods(5, CalendarUnitEnum.YEAR, PeriodicityEnum.MONTHLY, 60);
    }

    /**
     * Срок страхования - 5 лет.
     * Выплаты - ежеквартально.
     */
    @Test
    public void shouldBe1PeriodYears() {
        shouldBeNumberPeriods(5, CalendarUnitEnum.YEAR, PeriodicityEnum.ONCE, 1);
    }

    /**
     * Срок страхования - 36 месяцев.
     * Выплаты - ежегодно.
     */
    @Test
    public void shouldBe3Periods() {
        shouldBeNumberPeriods(36, CalendarUnitEnum.MONTH, PeriodicityEnum.YEARLY, 36);
    }

    /**
     * Срок страхования - 36 месяцев.
     * Выплаты - ежеквартально.
     */
    @Test
    public void shouldBe12Periods() {
        shouldBeNumberPeriods(36, CalendarUnitEnum.MONTH, PeriodicityEnum.QUARTERLY, 144);
    }

    /**
     * Срок страхования - 36 месяцев.
     * Выплаты - раз в полгода.
     */
    @Test
    public void shouldBe6Periods() {
        shouldBeNumberPeriods(36, CalendarUnitEnum.MONTH, PeriodicityEnum.TWICE_A_YEAR, 72);
    }

    /**
     * Срок страхования - 36 месяцев.
     * Выплаты - ежемесячно.
     */
    @Test
    public void shouldBe36Periods() {
        shouldBeNumberPeriods(2, CalendarUnitEnum.MONTH, PeriodicityEnum.MONTHLY, 24);
    }

    /**
     * Срок страхования - 36 месяцев.
     * Выплаты - единожды.
     */
    @Test
    public void shouldBe1PeriodMonths() {
        shouldBeNumberPeriods(36, CalendarUnitEnum.MONTH, PeriodicityEnum.ONCE, 1);
    }

    private void shouldBeNumberPeriods(Integer duration, CalendarUnitEnum calendarUnit, PeriodicityEnum periodicity, int shouldBeCountPeriods) {
        BuyoutService buyoutService = new BuyoutService(new RedemptionService(new RedemptionRepositoryStub()));

        Insurance insuranceContract = new Insurance();
        insuranceContract.setStartDate(LocalDate.of(2018, 1, 1));
        insuranceContract.setDuration(duration);
        insuranceContract.setCalendarUnit(calendarUnit);
        insuranceContract.setAmount(BigDecimal.valueOf(100000));
        insuranceContract.setPremium(BigDecimal.valueOf(10000));
        insuranceContract.setPeriodicity(periodicity);
        insuranceContract.setCurrency(1L);


        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setId(1L);
        programSetting.setProgram(program);


        insuranceContract.setProgramSetting(programSetting);
        List<ReportableRedemption> insurancePremium = buyoutService.getRedemptionByContract(insuranceContract, "RUB");

        assertEquals(insurancePremium.size(), shouldBeCountPeriods);
        assertEquals(insurancePremium.get(0).getPeriodNumber(), "1");
        assertNotNull(insurancePremium.get(0).getStartPeriod());
        assertEquals(insurancePremium.get(0).getStartPeriod(), LocalDate.of(2018, 1, 1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertNotNull(insurancePremium.get(0).getEndPeriod());
        assertEquals(insurancePremium.get(insurancePremium.size()-1).getPeriodNumber(), String.valueOf(insurancePremium.size()));
    }

    @Test
    public void shouldBe4PeriodsInPayString() {
        shouldBeNumberPeriodsInPayString(PeriodicityEnum.QUARTERLY, 4, 15);
    }

    @Test
    public void shouldBe2PeriodsInPayString() {
        shouldBeNumberPeriodsInPayString(PeriodicityEnum.TWICE_A_YEAR, 2, 10);
    }

    @Test
    public void shouldBe12PeriodsInPayString() {
        shouldBeNumberPeriodsInPayString(PeriodicityEnum.MONTHLY, 12, 31);
    }


    @Test
    public void shouldBe1PeriodInPayString() {
        shouldBeNumberPeriodsInPayString(PeriodicityEnum.YEARLY, 1, 1);
        shouldBeNumberPeriodsInPayString(PeriodicityEnum.ONCE, 1, 1);
    }

    private void shouldBeNumberPeriodsInPayString(PeriodicityEnum periodicity, int shouldBeCountPeriods, int dayNumber) {
        BuyoutService buyoutService = new BuyoutService(new RedemptionService(new RedemptionRepositoryStub()));

        Insurance insuranceContract = new Insurance();
        insuranceContract.setConclusionDate(LocalDate.of(2015, 8, dayNumber));
        insuranceContract.setPeriodicity(periodicity);

        String payDateString = buyoutService.getPayDateString(insuranceContract);

        assertNotNull(payDateString);
        int countPeriods = payDateString.split(", ").length;
        assertEquals(countPeriods, shouldBeCountPeriods);

        assertFalse(payDateString.contains("29 февраля")); // check pay date, if leap year
        assertFalse(payDateString.matches("0\\d")); // check leading zero in day values 01 jan, 02 feb, 03 march, etc... cases
        assertTrue(payDateString.endsWith("каждого года уплаты взносов"));
        System.out.println(payDateString);
    }

}
