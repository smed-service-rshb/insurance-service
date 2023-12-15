package ru.softlab.efr.services.insurance.model.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.model.enums.CompanyEnum.*;

public enum InsuranceStatusCode {
    MADE("Оформлен", Arrays.asList(RSHBINS, RSHBINSLIFE, SMS)),
    DRAFT("Черновик", Arrays.asList(RSHBINS, RSHBINSLIFE, SMS)),
    /*Специфичные статусы для компании РСХБ-Страхование жизни*/
    PROJECT("Проект", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    MADE_NOT_COMPLETED("Оформление не завершено", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    NEED_WITHDRAW_APPLICATION("Требуется заявление о выплате", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    WITHDRAW_APPLICATION_RECEIVED("Получено заявление о выплате", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    PAYMENT_FULFILLED("Выплата произведена", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    CHANGING_APPLICATION_RECEIVED("Получено заявление на изменение договора", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    REFUSING_APPLICATION_RECEIVED("Получено заявление об отказе", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    CLIENT_REFUSED("Отказ клиента", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    CANCELLATION_APPLICATION_RECEIVED("Получено заявление о расторжении договора", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    CANCELED("Расторгнут", Arrays.asList(RSHBINS, RSHBINSLIFE, SMS)),
    CANCELED_IN_HOLD_PERIOD("Расторгнут в период охлаждения", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    REVOKED("Аннулирован", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    REVOKED_REPLACEMENT("Аннулирование по замене", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    FINISHED("Окончен", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    /*Специфичные статусы для компании РСХБ-Страхование*/
    NOT_PAYED("Не оплачен", Collections.singletonList(RSHBINS)),
    NOT_ACCEPTED("Не акцептован", Arrays.asList(RSHBINS, RSHBINSLIFE)),
    PAYED("Оплачен", Arrays.asList(RSHBINS, RSHBINSLIFE, SMS)),
    /*Специфичные статусы для компании СМС*/
    CRM_IMPORTED("Выгружен в CRM", Collections.singletonList(SMS));
    //CRM_EDIT("")

    private String nameStatus;
    private List<CompanyEnum> companies;

    InsuranceStatusCode(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    InsuranceStatusCode(String nameStatus, List<CompanyEnum> companies) {
        this.nameStatus = nameStatus;
        this.companies = companies;
    }

    public static InsuranceStatusCode fromValue(String text) {
        for (InsuranceStatusCode b : InsuranceStatusCode.values()) {
            if (String.valueOf(b.nameStatus).equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public static InsuranceStatusCode findByValueAndCompany(String text, CompanyEnum companyEnum) {
        return Arrays.stream(InsuranceStatusCode.values())
                .filter(statusCode -> statusCode.nameStatus.equalsIgnoreCase(text) && statusCode.companies.contains(companyEnum))
                .findFirst().orElse(null);
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public static boolean isStatementStatus(InsuranceStatusCode checkingEnum) {
        final List<InsuranceStatusCode> statements = Collections.unmodifiableList(Arrays.asList(
                NEED_WITHDRAW_APPLICATION,
                WITHDRAW_APPLICATION_RECEIVED,
                CHANGING_APPLICATION_RECEIVED,
                REFUSING_APPLICATION_RECEIVED,
                CANCELLATION_APPLICATION_RECEIVED));

        return statements.contains(checkingEnum);
    }

    public static boolean isClosingStatus(InsuranceStatusCode statusCode) {
        final List<InsuranceStatusCode> closingStatusList = Arrays.asList(
                FINISHED,
                CLIENT_REFUSED,
                REVOKED,
                CANCELED_IN_HOLD_PERIOD,
                CANCELED);
        return closingStatusList.contains(statusCode);
    }

    public static List<InsuranceStatusCode> valuesByCompany(CompanyEnum company) {
        return Arrays.stream(InsuranceStatusCode.values()).
                filter(statusCode -> statusCode.companies.contains(company)).collect(Collectors.toList());
    }
}
