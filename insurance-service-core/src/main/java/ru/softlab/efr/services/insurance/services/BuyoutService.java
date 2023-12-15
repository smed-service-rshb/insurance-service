package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.RedemptionEntity;
import ru.softlab.efr.services.insurance.model.db.ReportableRedemption;
import ru.softlab.efr.services.insurance.model.enums.CalendarUnitEnum;
import ru.softlab.efr.services.insurance.model.enums.PeriodicityEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Сервис вычисления таблицы выкупных сумм по договору и справочнику выкупных коэффициентов
 *
 * @author olshansky
 * @since 04.12.2018
 */
@Service
public class BuyoutService {
    private static final Logger LOGGER = Logger.getLogger(BuyoutService.class);

    private final RedemptionService redemptionService;

    @Autowired
    public BuyoutService(RedemptionService redemptionService) {
        this.redemptionService = redemptionService;
    }

    /**
     * Получить список элементов типа "выкупная сумма" для формирования печатной формы
     *
     * @param insuranceContract договор страхования
     */
    public List<ReportableRedemption> getRedemptionByContract(Insurance insuranceContract, String contractCurrency) {
        List<ReportableRedemption> redemptionList = new ArrayList<>();
        RedemptionEntity redemption = redemptionService.findRedemption(
                insuranceContract.getProgramSetting().getProgram().getId(),
                insuranceContract.getDuration(),
                insuranceContract.getCurrency(),
                insuranceContract.getPeriodicity());
        if (redemption != null) {
            IntStream.rangeClosed(1, redemption.getCoefficientList().size()).forEach(numberYear -> {

                String periodNumber = String.valueOf(numberYear);
                String beginPeriod = "";
                String endPeriod = "";
                String redemptionAmount = "";
                try {
                    beginPeriod = ReportableContract.presentLocalDate(getBeginPeriod(numberYear, insuranceContract, redemption));
                    endPeriod = ReportableContract.presentLocalDate(getEndPeriod(numberYear, insuranceContract, redemption));
                    redemptionAmount = ReportableContract.presentBigDecimal(getRedemptionAmount(numberYear, insuranceContract));
                } catch (Exception ex) {
                    LOGGER.error("При расчёте периода и выкупной суммы произошла ошибка", ex);
                }
                redemptionList.add(new ReportableRedemption(
                        periodNumber,
                        beginPeriod,
                        endPeriod,
                        redemptionAmount,
                        contractCurrency
                ));

            });
        }
        return redemptionList;
    }

    /**
     * ТЗ Рента 2.7.3 Формирование даты уплаты страховых взносов.
     */
    public String getPayDateString(Insurance insuranceContract) {
        PeriodicityEnum periodicity = insuranceContract.getPeriodicity() != null ? insuranceContract.getPeriodicity() : PeriodicityEnum.YEARLY;

        return IntStream.rangeClosed(0, getCountPayPeriodsByPeriodicity(periodicity) - 1).mapToObj(i -> {
            LocalDate resultDate = ReportableContract.getPayStart(insuranceContract).plusMonths(getStepPeriod(periodicity) * i);
            if (resultDate.getDayOfMonth() == 29 && resultDate.getMonthValue() == 2) {
                resultDate = resultDate.minusDays(1);
            }
            return ReportableContract.presentLocalDate(resultDate, "d MMMM");
        }).collect(Collectors.joining(", ")).concat(" каждого года уплаты взносов");
    }

    private static int getStepPeriod(PeriodicityEnum periodicity) {
        if (periodicity.equals(PeriodicityEnum.YEARLY)) {
            return 0;
        } else if (periodicity.equals(PeriodicityEnum.QUARTERLY)) {
            return 3;
        } else if (periodicity.equals(PeriodicityEnum.TWICE_A_YEAR)) {
            return 6;
        }
        return 1;
    }

    private int getMonthsInPaymentPeriod(PeriodicityEnum periodicityEnum) {
        if (periodicityEnum.equals(PeriodicityEnum.YEARLY)) {
            return 12;
        } else if (periodicityEnum.equals(PeriodicityEnum.QUARTERLY)) {
            return 3;
        } else if (periodicityEnum.equals(PeriodicityEnum.TWICE_A_YEAR)) {
            return 6;
        }
        return 1;
    }

    /**
     * Получить начало периода страхования по номеру периода и периодичности
     * Начало периода рассчитывается как:
     * dn=Dn-1 + 1 день, где:
     * o	Dn-1 – дата окончания действия (n-1) периода.
     * o	dn – дата начала действия n-ного периода.
     *
     * @param numberYear
     * @param insuranceContract
     * @return Начало периода
     */
    private LocalDate getBeginPeriod(Integer numberYear, Insurance insuranceContract, RedemptionEntity redemption) {
        if (numberYear.equals(1)) {
            return insuranceContract.getStartDate();
        } else {
            return getEndPeriod(numberYear - 1, insuranceContract, redemption).plusDays(1);
        }
    }

    /**
     * Получить окончание периода страхования по номеру периода и периодичности
     * Dn=dn + N – 1 день, где:
     * o	Dn – дата окончания действия n-ного периода.
     * o	dn – дата начала действия n-ного периода.
     * o	N – количество месяцев в периоде уплаты взносов.
     *
     * @param numberYear
     * @param insuranceContract
     * @return Окончание периода страхования
     */
    private LocalDate getEndPeriod(Integer numberYear, Insurance insuranceContract, RedemptionEntity redemption) {
        if (redemption.getPaymentPeriod().equals(PeriodicityEnum.ONCE)) {
            if (insuranceContract.getCloseDate() == null) {
                if (insuranceContract.getCalendarUnit().equals(CalendarUnitEnum.YEAR)) {
                    return insuranceContract.getStartDate().plusYears(insuranceContract.getDuration());
                } else if (insuranceContract.getCalendarUnit().equals(CalendarUnitEnum.MONTH)) {
                    return insuranceContract.getStartDate().plusMonths(insuranceContract.getDuration());
                } else if (insuranceContract.getCalendarUnit().equals(CalendarUnitEnum.DAY)) {
                    return insuranceContract.getStartDate().plusDays(insuranceContract.getDuration());
                }
            }
            return insuranceContract.getCloseDate();
        }
        return getBeginPeriod(numberYear, insuranceContract, redemption).plusMonths(getMonthsInPaymentPeriod(redemption.getPaymentPeriod())).minusDays(1);
    }

    /**
     * Получить размер выкупной суммы для i периода
     * должен рассчитываться следующим образом:
     * ВСi = СРежемесячная*K, где:
     * o	ВСi – выкупная сумма за i период расчета.
     * o	CPежемесячная – разовая страховая премия по договору.
     * o	K – коэффициент, заданный в справочнике коэффициентов для расчета выкупных сумм.
     * Коэффициент определяется исходя из программы страхования,
     * срока действия программы страхования, валюты договора,
     * периодичности уплаты взносов, периода расчета (п.2.6).
     *
     * @param numberYear
     * @return размер выкупной суммы для i периода
     */
    private BigDecimal getRedemptionAmount(Integer numberYear, Insurance insuranceContract) {
        return insuranceContract.getPremium().multiply(redemptionService.findRate(
                insuranceContract.getProgramSetting().getProgram().getId(),
                insuranceContract.getDuration(),
                insuranceContract.getCurrency(),
                insuranceContract.getPeriodicity(),
                numberYear
        ));
    }

    private int getCountPayPeriodsByPeriodicity(PeriodicityEnum periodicity) {
        if (periodicity.equals(PeriodicityEnum.QUARTERLY)) {
            return 4;
        } else if (periodicity.equals(PeriodicityEnum.TWICE_A_YEAR)) {
            return 2;
        } else if (periodicity.equals(PeriodicityEnum.MONTHLY)) {
            return 12;
        } else {
            return 1;
        }
    }

    private int getTopRangeByDurationAndPeriodicity(Integer insuranceDuration, PeriodicityEnum periodicity, CalendarUnitEnum calendarUnit) {
        if (periodicity == null) {
            throw new RuntimeException("Произошла ошибка во время вычисления верхнего диапазона периода выкупной суммы - periodicity не может быть null");
        }
        if (insuranceDuration == null || insuranceDuration.equals(0)) {
            throw new RuntimeException("Произошла ошибка во время вычисления верхнего диапазона периода выкупной суммы - insuranceDuration не может быть null или 0");
        }
        if (calendarUnit == null) {
            throw new RuntimeException("Произошла ошибка во время вычисления верхнего диапазона периода выкупной суммы - calendarUnit не может быть null");
        }
        if (periodicity.equals(PeriodicityEnum.ONCE)) {
            return 1;
        } else if (periodicity.equals(PeriodicityEnum.YEARLY) && calendarUnit.equals(CalendarUnitEnum.YEAR)
                || periodicity.equals(PeriodicityEnum.MONTHLY) && calendarUnit.equals(CalendarUnitEnum.MONTH)) {
            return insuranceDuration;
        } else if (calendarUnit.equals(CalendarUnitEnum.MONTH)) {
            return insuranceDuration / 12 * getCountPayPeriodsByPeriodicity(periodicity);
        } else if (calendarUnit.equals(CalendarUnitEnum.YEAR)) {
            return insuranceDuration * getCountPayPeriodsByPeriodicity(periodicity);
        } else if (calendarUnit.equals(CalendarUnitEnum.DAY)) {
            return insuranceDuration / getDaysInYear() * getCountPayPeriodsByPeriodicity(periodicity);
        }
        return 1;
    }

    private int getDaysInYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }
}
