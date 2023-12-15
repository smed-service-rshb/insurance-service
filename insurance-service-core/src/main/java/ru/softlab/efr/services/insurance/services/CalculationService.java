package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.model.rest.RiskSum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис расчётов
 *
 * @author olshansky
 * @since 23.10.2018
 */
@Service
public class CalculationService {


    @Autowired
    private ProgramSettingService programSettingService;

    @Autowired
    private RiskSettingService riskSettingService;

    private static final int scale = 2;

    /**
     * Рассчитать страховую сумму для выбранных параметров программы страхования и указанной суммы страховой премии
     * за один период внесения взносов.
     *
     * @param programSetting   Параметры программы страхования.
     * @param premiumPerPeriod Сумма страхового взноса за один период.
     * @param calendarUnit     Календарная единицы срока страхования.
     * @param term             Срок страхования (в календарных единицах).
     * @return Рассчитанная страховая сумма.
     */
    public BigDecimal getInsuranceAmountByProgramSetting(ProgramSetting programSetting, BigDecimal premiumPerPeriod,
                                                         CalendarUnitEnum calendarUnit, int term) {
        int premiumPayPeriodCount;
        switch (programSetting.getPeriodicity()) {
            case ONCE:
                premiumPayPeriodCount = 1;
                break;
            case YEARLY:
                int termInMonth = termInMonth(calendarUnit, term);
                premiumPayPeriodCount = termInMonth / 12;
                break;
            case TWICE_A_YEAR:
                premiumPayPeriodCount = termInMonth(calendarUnit, term) / 6;
                break;
            case QUARTERLY:
                premiumPayPeriodCount = termInMonth(calendarUnit, term) / 3;
                break;
            case MONTHLY:
            default:
                premiumPayPeriodCount = termInMonth(calendarUnit, term);
                break;
        }

        BigDecimal totalPremium = premiumPerPeriod;
        if(ProgramKind.RENT != programSetting.getProgram().getType()) {
            totalPremium = premiumPerPeriod.multiply(BigDecimal.valueOf(premiumPayPeriodCount));
        }

        if (PremiumMethodEnum.BY_FORMULA.equals(programSetting.getPremiumMethod())) {
            return programSetting.getTariff().multiply(totalPremium);
        } else if (PremiumMethodEnum.FIXED.equals(programSetting.getPremiumMethod())) {
            return programSetting.getInsuranceAmount();
        }
        // TODO : реализовать обработку варианта BY_RISK

        return BigDecimal.ZERO;
    }

    /**
     * Рассчитать сумму страховой премии за весь период действия договора для выбранных параметров программы
     * страхования и указанной страховой суммы.
     *
     * @param programSetting  Параметры программы страхования.
     * @param insuranceAmount Страховая сумма.
     * @return Рассчитанная страховая премия за весь период действия договора.
     */
    public BigDecimal getInsurancePremiumByProgramSetting(ProgramSetting programSetting, BigDecimal insuranceAmount) {
        if (PremiumMethodEnum.BY_FORMULA.equals(programSetting.getPremiumMethod())) {
            return insuranceAmount.divide(programSetting.getTariff(), scale, RoundingMode.HALF_UP);
        } else if (PremiumMethodEnum.FIXED.equals(programSetting.getPremiumMethod())) {
            return programSetting.getBonusAmount();
        } else { // TODO: обработать вариант BY_RISK
            return BigDecimal.ZERO;
        }
    }

    /**
     * Рассчитать сумму страховой премии за один период внесения взносов для выбранных параметров программы
     * страхования и указанной страховой суммы.
     *
     * @param programSetting  Параметры программы страхования.
     * @param insuranceAmount Страховая сумма.
     * @param calendarUnit    Календарная единицы срока страхования.
     * @param term            Срок страхования (в календарных единицах).
     * @return Рассчитанная страховая премия за один период.
     */
    public BigDecimal getInsurancePremiumPerPeriodByProgramSetting(ProgramSetting programSetting, BigDecimal insuranceAmount,
                                                                   CalendarUnitEnum calendarUnit, int term) {
        BigDecimal totalPremium = getInsurancePremiumByProgramSetting(programSetting, insuranceAmount);

        // Рассчитываем количество периодов оплаты, которые приходятся на срок страхования, и выполняем
        // деление суммы страховой премии на количество периодов.
        int periodCount = periodCount(calendarUnit, term, programSetting.getPeriodicity());

        if (periodCount == 1 || ProgramKind.RENT.equals(programSetting.getProgram().getType())) {
            return totalPremium;
        } else {
            return totalPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP);
        }
    }

    /**
     * Рассчитать количество периодов оплаты страховой премии во время периода страхования.
     *
     * @param calendarUnit    Календарная единицы срока страхования.
     * @param term            Срок страхования (в календарных единицах).
     * @param periodicityEnum Периодичность оплаты стаховой премии.
     * @return Количество периодов оплаты страховой премии во время периода страхования.
     */
    public int periodCount(CalendarUnitEnum calendarUnit, int term, PeriodicityEnum periodicityEnum) {
        switch (periodicityEnum) {
            case ONCE:
                return 1;
            case YEARLY:
                return termInMonth(calendarUnit, term) / 12;
            case TWICE_A_YEAR:
                return termInMonth(calendarUnit, term) / 6;
            case QUARTERLY:
                return termInMonth(calendarUnit, term) / 3;
            case MONTHLY:
            default:
                return termInMonth(calendarUnit, term);
        }
    }

    private int termInMonth(CalendarUnitEnum calendarUnit, int term) {
        switch (calendarUnit) {
            case YEAR:
                return term * 12;
            case MONTH:
                return term;
            case DAY:
            default:
                //FIXME: не понятно как переводит количество дней в месяцы
                return term / 30;
        }
    }

    /**
     * Расчёт страховой суммы по риску.
     *
     * @param riskSetting     Настройки риска.
     * @param contractAmount  Страховая сумма по договору.
     * @param contractPremium Страховая премия по договору.
     * @param periodCount     Количество периодов оплаты страховой премии.
     * @return Страховая сумма по риску или null, если в соотвествии с настройками страховая сумма не должна
     * рассчитываться.
     */
    public BigDecimal getInsuranceAmountByRiskSetting(RiskSetting riskSetting, BigDecimal contractAmount, BigDecimal contractPremium, int periodCount) {
        BigDecimal ammount = null;
        if (RiskCalculationTypeEnum.NOT_CALCULATED.equals(riskSetting.getCalculationType())) {
            return null;
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_TERM.equals(riskSetting.getCalculationType())) {
            return null;
        } else if (RiskCalculationTypeEnum.FIXED.equals(riskSetting.getCalculationType())) {
            ammount = riskSetting.getRiskAmount();
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_RISK.equals(riskSetting.getCalculationType())) {
            BigDecimal dependenceRiskInsuranceAmount = getInsuranceAmountByRiskSetting(riskSetting.getRiskDependence(), contractAmount, contractPremium, periodCount);
            if (dependenceRiskInsuranceAmount == null) {
                return null;
            }
            ammount = dependenceRiskInsuranceAmount.multiply(riskSetting.getCalculationCoefficient());
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_SUM.equals(riskSetting.getCalculationType())) {
            ammount = contractAmount.multiply(riskSetting.getCalculationCoefficient());
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM.equals(riskSetting.getCalculationType())) {
            if (RiskRecordAmountTypeEnum.SINGLE_PREMIUM.equals(riskSetting.getRecordAmount())) {
                contractPremium = contractPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP);
            }
            if (RiskCalculationSumTypeEnum.CONSTANT.equals(riskSetting.getType())) {
                ammount = contractPremium.multiply(riskSetting.getCalculationCoefficient());
            } else if (RiskCalculationSumTypeEnum.DECREASING.equals(riskSetting.getType())) {
                // Необходимо возвратить страховую сумму за первый период; она равна страховой премии за весь период
                // за вычетом страховой премии за один период.
                ammount = contractPremium.subtract(contractPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP));
            } else if (RiskCalculationSumTypeEnum.INCREASING.equals(riskSetting.getType())) {
                // Необходимо возвратить страховую сумму за первый период; она равна страховой премии за один период.
                ammount = contractPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP);
            }
        }
        ammount = riskSetting.getMaxRiskAmount() != null && ammount != null &&
                ammount.compareTo(riskSetting.getMaxRiskAmount()) > 0 ? riskSetting.getMaxRiskAmount() : ammount;
        return ammount;
    }

    /**
     * Рассчёт страховой премии по риску.
     *
     * @param riskSetting Настройки риска.
     * @return Страховая премия по риску или null, если в соотвествии с настройками страховая премия не должна
     * рассчитываться.
     */
    public BigDecimal getInsurancePremiumByRiskSetting(RiskSetting riskSetting) {
        if (RiskCalculationTypeEnum.NOT_CALCULATED.equals(riskSetting.getCalculationType())) {
            return null;
        } else if (RiskCalculationTypeEnum.FIXED.equals(riskSetting.getCalculationType())) {
            return riskSetting.getRiskPremium();
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_RISK.equals(riskSetting.getCalculationType())) {
            return null;
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_SUM.equals(riskSetting.getCalculationType())) {
            return null;
        } else if (RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM.equals(riskSetting.getCalculationType())) {
            return null;
        }
        // TODO: необходимо реализовать варианты расчёта BY_FORMULA BY_COMPLEX_FORMULA
        return null;
    }

    /**
     * Рассчёт страховой суммы в разбивке по периодам оплаты страховых взносов.
     *
     * @param riskSetting  Настройки риска.
     * @param totalPremium Страховая премия по договору.
     * @param periodCount  Количество периодов оплаты страховой премии.
     * @return Страховая сумма в разбивке по периодам оплаты страховых взносов или null, если разбивки нет в
     * соответствии с настройками риска.
     */
    public List<RiskSum> getRiskAmounts(RiskSetting riskSetting, BigDecimal totalPremium, int periodCount) {

        if (RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM.equals(riskSetting.getCalculationType())) {
            if (RiskCalculationSumTypeEnum.DECREASING.equals(riskSetting.getType())) {
                List<RiskSum> riskSums = new ArrayList<>();
                BigDecimal premiumPerPeriod = totalPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP);
                for (int i = 1; i <= periodCount; i++) {
                    RiskSum riskSum = new RiskSum();
                    riskSum.setPeriodNumber(i);
                    riskSum.setAmount(totalPremium.subtract(premiumPerPeriod.multiply(BigDecimal.valueOf(i))));
                    riskSums.add(riskSum);
                }
                return riskSums;
            } else if (RiskCalculationSumTypeEnum.INCREASING.equals(riskSetting.getType())) {
                List<RiskSum> riskSums = new ArrayList<>();
                BigDecimal premiumPerPeriod = totalPremium.divide(BigDecimal.valueOf(periodCount), scale, RoundingMode.HALF_UP);
                for (int i = 1; i <= periodCount; i++) {
                    RiskSum riskSum = new RiskSum();
                    riskSum.setPeriodNumber(i);
                    riskSum.setAmount(premiumPerPeriod.multiply(BigDecimal.valueOf(i)));
                    riskSums.add(riskSum);
                }
                return riskSums;
            }
        }

        return null;
    }
}
