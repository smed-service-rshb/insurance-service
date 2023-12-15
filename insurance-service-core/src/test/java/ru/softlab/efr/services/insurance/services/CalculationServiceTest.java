package ru.softlab.efr.services.insurance.services;

import org.junit.Test;
import ru.softlab.efr.services.insurance.model.db.Program;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.RiskSum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Тесты для проверки расчётов страховых сумм и страховых премий.
 *
 * @author Andrey Grigorov
 */
public class CalculationServiceTest {

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Единовременно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFixedOnce() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.ONCE);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount, insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежемесячно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFixedMonthly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.MONTHLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP), insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежегодно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFixedYearly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.YEARLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP), insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Раз в полгода".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFixedTwiceAYear() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.TWICE_A_YEAR);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP), insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежеквартально".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFixedQuarterly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.QUARTERLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.divide(BigDecimal.valueOf(20), 2, RoundingMode.HALF_UP), insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Единовременно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFormulaOnce() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.ONCE);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(insuranceAmount.divide(tariff, 2, RoundingMode.HALF_UP), insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Ежемесячно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFormulaMonthly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.MONTHLY);
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        BigDecimal expected = insuranceAmount
                .divide(tariff, 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        assertEquals(expected, insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Ежегодно".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFormulaYearly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.YEARLY);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        BigDecimal expected = insuranceAmount
                .divide(tariff, 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP);
        assertEquals(expected, insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Раз в полгода".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFormulaTwiceAYear() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.TWICE_A_YEAR);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        BigDecimal expected = insuranceAmount
                .divide(tariff, 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(10), 2, RoundingMode.HALF_UP);
        assertEquals(expected, insurancePremium);
    }

    /**
     * Расчёт суммы страховой премии за один период уплаты взносов, если в настройках параметров программы
     * страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Раз в полгода".
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsurancePremiumByFormulaQuarterly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.QUARTERLY);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal insuranceAmount = new BigDecimal("20000.10");
        BigDecimal insurancePremium = calculationService.getInsurancePremiumPerPeriodByProgramSetting(
                programSetting, insuranceAmount, CalendarUnitEnum.YEAR, 5);

        BigDecimal expected = insuranceAmount
                .divide(tariff, 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(20), 2, RoundingMode.HALF_UP);
        assertEquals(expected, insurancePremium);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Единовременно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFixedOnce() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.ONCE);
        BigDecimal premiumPerPeriod = new BigDecimal("10000.55");
        programSetting.setBonusAmount(premiumPerPeriod);
        BigDecimal fixedInsuranceAmount = new BigDecimal("20000.10");
        programSetting.setInsuranceAmount(fixedInsuranceAmount);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, premiumPerPeriod, CalendarUnitEnum.YEAR, 5);

        assertEquals(fixedInsuranceAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежемесячно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFixedMonthly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.MONTHLY);
        BigDecimal premiumPerPeriod = new BigDecimal("10000.55");
        programSetting.setBonusAmount(premiumPerPeriod);
        BigDecimal fixedInsuranceAmount = new BigDecimal("20000.10");
        programSetting.setInsuranceAmount(fixedInsuranceAmount);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, premiumPerPeriod, CalendarUnitEnum.YEAR, 5);

        assertEquals(fixedInsuranceAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежегодно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFixedYearly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.YEARLY);
        BigDecimal premiumPerPeriod = new BigDecimal("10000.55");
        programSetting.setBonusAmount(premiumPerPeriod);
        BigDecimal fixedInsuranceAmount = new BigDecimal("20000.10");
        programSetting.setInsuranceAmount(fixedInsuranceAmount);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, premiumPerPeriod, CalendarUnitEnum.YEAR, 5);

        assertEquals(fixedInsuranceAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Раз в полгода";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFixedTwiceAYear() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.TWICE_A_YEAR);
        BigDecimal premiumPerPeriod = new BigDecimal("10000.55");
        programSetting.setBonusAmount(premiumPerPeriod);
        BigDecimal fixedInsuranceAmount = new BigDecimal("20000.10");
        programSetting.setInsuranceAmount(fixedInsuranceAmount);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, premiumPerPeriod, CalendarUnitEnum.YEAR, 5);

        assertEquals(fixedInsuranceAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "фиксированное значение";
     * "Периодичность уплаты взносов" = "Ежеквартально";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFixedQuarterly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.FIXED);
        programSetting.setPeriodicity(PeriodicityEnum.QUARTERLY);
        BigDecimal premiumPerPeriod = new BigDecimal("10000.55");
        programSetting.setBonusAmount(premiumPerPeriod);
        BigDecimal fixedInsuranceAmount = new BigDecimal("20000.10");
        programSetting.setInsuranceAmount(fixedInsuranceAmount);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, premiumPerPeriod, CalendarUnitEnum.YEAR, 5);

        assertEquals(fixedInsuranceAmount, insuranceAmount);
    }


    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Единовременно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFormulaOnce() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.ONCE);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, bonusAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.multiply(tariff), insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Ежемесячно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFormulaMonthly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.MONTHLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, bonusAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.multiply(tariff).multiply(BigDecimal.valueOf(60)), insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Ежегодно";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFormulaYearly() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.YEARLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, bonusAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.multiply(tariff).multiply(BigDecimal.valueOf(5)), insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Раз в полгода";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFormulaTwiceAYear() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.TWICE_A_YEAR);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, bonusAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.multiply(tariff).multiply(BigDecimal.valueOf(10)), insuranceAmount);
    }

    /**
     * Расчёт страховой суммы, если в настройках параметров программы страхования указаны следующие значения:
     * "Тип расчета СС и СП" = "по формуле";
     * "Периодичность уплаты взносов" = "Ежеквартально";
     * Срок страхования - 5 лет.
     */
    @Test
    public void calculateInsuranceAmountByFormulaQuartely() {
        CalculationService calculationService = new CalculationService();
        ProgramSetting programSetting = new ProgramSetting();
        Program program = new Program();
        program.setType(ProgramKind.ISJ);
        programSetting.setProgram(program);
        programSetting.setPremiumMethod(PremiumMethodEnum.BY_FORMULA);
        programSetting.setPeriodicity(PeriodicityEnum.QUARTERLY);
        BigDecimal bonusAmount = new BigDecimal("10000.55");
        programSetting.setBonusAmount(bonusAmount);
        BigDecimal tariff = new BigDecimal("5");
        programSetting.setTariff(tariff);
        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByProgramSetting(
                programSetting, bonusAmount, CalendarUnitEnum.YEAR, 5);

        assertEquals(bonusAmount.multiply(tariff).multiply(BigDecimal.valueOf(20)), insuranceAmount);
    }

    /**
     * Расчёт страховой премии по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Не рассчитывается";
     * <p>
     * В этом случае СП риска не расчитывается.
     */
    @Test
    public void calculateRiskPremiumNonCalculated() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.NOT_CALCULATED);

        BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(riskSetting);

        assertNull(riskPremium);
    }

    /**
     * Расчёт страховой премии по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Фиксированные значения";
     * <p>
     * В этом случае страховая премия для риска должна быть жестко задана для риска в поле «Страховая премия по риску».
     */
    @Test
    public void calculateRiskPremiumFixed() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.FIXED);
        BigDecimal premium = new BigDecimal("1000.00");
        riskSetting.setRiskPremium(premium);

        BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(riskSetting);

        assertEquals(premium, riskPremium);
    }

    /**
     * Расчёт страховой премии по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СС другого риска";
     * <p>
     * В этом случае СП риска не расчитывается.
     */
    @Test
    public void calculateRiskPremiumDependsOnRisk() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_RISK);

        BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(riskSetting);

        assertNull(riskPremium);
    }

    /**
     * Расчёт страховой премии по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СС договора";
     * <p>
     * В этом случае СП риска не расчитывается.
     */
    @Test
    public void calculateRiskPremiumDependsOnContractSum() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_SUM);

        BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(riskSetting);

        assertNull(riskPremium);
    }

    /**
     * Расчёт страховой премии по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * <p>
     * В этом случае СП риска не расчитывается.
     */
    @Test
    public void calculateRiskPremiumDependsOnContractPremium() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);

        BigDecimal riskPremium = calculationService.getInsurancePremiumByRiskSetting(riskSetting);

        assertNull(riskPremium);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Не рассчитывается";
     * <p>
     * В этом случае СС риска не расчитывается.
     */
    @Test
    public void calculateRiskAmountNonCalculated() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.NOT_CALCULATED);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("2000.00");
        int periodCount = 12;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        assertNull(insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Фиксированные значения";
     * <p>
     * В этом случае страховая сумма для риска должна быть жестко задана для риска в поле «Страховая сумма по риску».
     */
    @Test
    public void calculateRiskAmountFixed() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.FIXED);
        BigDecimal riskAmount = new BigDecimal("1000.00");
        riskSetting.setRiskAmount(riskAmount);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("2000.00");
        int periodCount = 12;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        assertEquals(riskAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СС другого риска";
     * <p>
     * В этом случае страховая сумма для риска равна страховой сумме другого риска, умноженной на тариф.
     */
    @Test
    public void calculateRiskAmountDependsOnRisk() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_RISK);
        BigDecimal calculationCoefficient = new BigDecimal("1.5");
        riskSetting.setCalculationCoefficient(calculationCoefficient);
        RiskSetting dependenceRiskSetting = new RiskSetting();
        dependenceRiskSetting.setCalculationType(RiskCalculationTypeEnum.FIXED);
        BigDecimal dependenceRiskAmount = new BigDecimal("1000");
        dependenceRiskSetting.setRiskAmount(dependenceRiskAmount);
        riskSetting.setRiskDependence(dependenceRiskSetting);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("2000.00");
        int periodCount = 12;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        BigDecimal expectedRiskAmount = dependenceRiskAmount.multiply(calculationCoefficient);
        assertEquals(expectedRiskAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СС договора";
     * <p>
     * В этом случае страховая сумма для риска равна страховой сумме договора, умноженной на тариф.
     */
    @Test
    public void calculateRiskAmountDependsOnContractSum() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_SUM);
        BigDecimal calculationCoefficient = new BigDecimal("0.5");
        riskSetting.setCalculationCoefficient(calculationCoefficient);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("2000.00");
        int periodCount = 12;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        BigDecimal expectedRiskAmount = contractAmount.multiply(calculationCoefficient);
        assertEquals(expectedRiskAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "постоянная".
     * <p>
     * В этом случае страховая сумма для риска равна премии договора, умноженной на коэффициент расчёта.
     */
    @Test
    public void calculateRiskAmountDependsOnContractPremiumConstant() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.CONSTANT);
        BigDecimal calculationCoefficient = new BigDecimal("0.5");
        riskSetting.setCalculationCoefficient(calculationCoefficient);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("2000.00");
        int periodCount = 12;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        BigDecimal expectedRiskAmount = contractPremium.multiply(calculationCoefficient);
        assertEquals(expectedRiskAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "убывающая".
     * <p>
     * В этом случае страховая сумма для первого периода равна страховой премии за весь период за вычетом страховой
     * премии за один период.
     */
    @Test
    public void calculateRiskAmountDependsOnContractPremiumDecreasing() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.DECREASING);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("5000.00");
        int periodCount = 5;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        BigDecimal expectedRiskAmount = new BigDecimal("4000.00");
        assertEquals(expectedRiskAmount, insuranceAmount);
    }

    /**
     * Расчёт страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "возрастающая".
     * <p>
     * В этом случае страховая сумма для первого периода равна страховой премии за один период.
     */
    @Test
    public void calculateRiskAmountDependsOnContractPremiumIncreasing() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.INCREASING);
        BigDecimal contractAmount = new BigDecimal("10000.00");
        BigDecimal contractPremium = new BigDecimal("5000.00");
        int periodCount = 5;

        BigDecimal insuranceAmount = calculationService.getInsuranceAmountByRiskSetting(riskSetting, contractAmount, contractPremium, periodCount);

        BigDecimal expectedRiskAmount = new BigDecimal("1000.00");
        assertEquals(expectedRiskAmount, insuranceAmount);
    }

    /**
     * Расчет разбивки страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "убывающая".
     */
    @Test
    public void getRiskAmountsDecreasing() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.DECREASING);
        BigDecimal contractPremium = new BigDecimal("3000.00");
        int periodCount = 3;

        List<RiskSum> riskAmounts = calculationService.getRiskAmounts(riskSetting, contractPremium, periodCount);

        assertEquals(periodCount, riskAmounts.size());
        assertEquals(new BigDecimal("2000.00"), riskAmounts.get(0).getAmount());
        assertEquals(1L, Long.valueOf(riskAmounts.get(0).getPeriodNumber()).longValue());
        assertEquals(new BigDecimal("1000.00"), riskAmounts.get(1).getAmount());
        assertEquals(2L, Long.valueOf(riskAmounts.get(1).getPeriodNumber()).longValue());
        assertEquals(new BigDecimal("0.00"), riskAmounts.get(2).getAmount());
        assertEquals(3L, Long.valueOf(riskAmounts.get(2).getPeriodNumber()).longValue());
    }

    /**
     * Расчет разбивки страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "возрастающая".
     */
    @Test
    public void getRiskAmountsIncreasing() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.INCREASING);
        BigDecimal contractPremium = new BigDecimal("3000.00");
        int periodCount = 3;

        List<RiskSum> riskAmounts = calculationService.getRiskAmounts(riskSetting, contractPremium, periodCount);

        assertEquals(periodCount, riskAmounts.size());
        assertEquals(new BigDecimal("1000.00"), riskAmounts.get(0).getAmount());
        assertEquals(1L, Long.valueOf(riskAmounts.get(0).getPeriodNumber()).longValue());
        assertEquals(new BigDecimal("2000.00"), riskAmounts.get(1).getAmount());
        assertEquals(2L, Long.valueOf(riskAmounts.get(1).getPeriodNumber()).longValue());
        assertEquals(new BigDecimal("3000.00"), riskAmounts.get(2).getAmount());
        assertEquals(3L, Long.valueOf(riskAmounts.get(2).getPeriodNumber()).longValue());
    }

    /**
     * Расчет разбивки страховой суммы по риску, если в настройках риска указаны следующие значения:
     * "Тип расчета СС и СП по риску" = "Зависит от СП договора";
     * "Тип расчёта" = "константа".
     * <p>
     * При таких настройках разбивка не выполняется.
     */
    @Test
    public void getRiskAmountsNull() {
        CalculationService calculationService = new CalculationService();
        RiskSetting riskSetting = new RiskSetting();
        riskSetting.setCalculationType(RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM);
        riskSetting.setType(RiskCalculationSumTypeEnum.CONSTANT);
        BigDecimal contractPremium = new BigDecimal("3000.00");
        int periodCount = 3;

        List<RiskSum> riskAmounts = calculationService.getRiskAmounts(riskSetting, contractPremium, periodCount);

        assertNull(riskAmounts);
    }
}