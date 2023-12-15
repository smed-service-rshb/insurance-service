package ru.softlab.efr.services.insurance.pojo;

import ru.softlab.efr.common.dict.exchange.model.PersonnelData;

/**
 * Результат проверки клиента по справочникам
 */
public class CheckResult {

    /**
     * Признак получения информации от сервиса справочников
     */
    private boolean responseReceived;
    /**
     * Данные клиента
     */
    private PersonnelData blockedPersonnelData;

    /**
     * Признак успешной проверки
     */
    private boolean checkSuccess;
    /**
     * Идентификатор проверки
     */
    private Long checkId;

    public CheckResult() {
    }

    public CheckResult(boolean hasCheckBlockagesRs, PersonnelData blockedPersonnelData, Long checkId) {
        this.responseReceived = hasCheckBlockagesRs;
        this.blockedPersonnelData = blockedPersonnelData;
        this.checkId = checkId;
    }

    public CheckResult(boolean responseReceived, PersonnelData blockedPersonnelData, boolean checkSuccess, Long checkId) {
        this.responseReceived = responseReceived;
        this.blockedPersonnelData = blockedPersonnelData;
        this.checkSuccess = checkSuccess;
        this.checkId = checkId;
    }

    public boolean isResponseReceived() {
        return responseReceived;
    }

    public PersonnelData getBlockedPersonnelData() {
        return blockedPersonnelData;
    }

    public Long getCheckId() {
        return checkId;
    }

    public boolean isCheckSuccess() {
        return checkSuccess;
    }
}
