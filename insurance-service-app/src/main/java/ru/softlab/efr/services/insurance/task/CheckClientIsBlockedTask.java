package ru.softlab.efr.services.insurance.task;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softlab.efr.clients.model.Client;
import ru.softlab.efr.common.dict.exchange.model.PersonnelData;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.pojo.CheckResult;
import ru.softlab.efr.services.insurance.services.CheckService;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.HashSet;
import java.util.Set;

/**
 * Обработчик проверки клиента по переченю лиц, в отношении которых действует решение межведомственной комиссии о
 * замораживании (блокировании) принадлежащих им денежных средств или иного имущества
 *
 * @author Krivenko
 * @since 20.09.2018
 */
@Component
public class CheckClientIsBlockedTask implements JavaDelegate {

    private final static String DOCUMENT_CHECK_SETTINGS = "documentDataChecks";

    private JsonHelper jsonHelper;

    @Autowired
    private CheckService checkService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if (Boolean.TRUE.equals(Boolean.valueOf(settingsService.get(DOCUMENT_CHECK_SETTINGS).getValue()))) {
            execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
            execution.setVariable(ContextVariablesNames.DECISION_DATA, null);
            OwnerType ownerType = (OwnerType) execution.getVariable(ContextVariablesNames.OWNER_TYPE);
            Client client = jsonHelper.deserialize((String) execution.getVariable(ContextVariablesNames.CLIENT_DATA), Client.class);
            CheckResult checkResult = null;
            try {
                checkResult = checkService.checkClientByBlockage(client);
            } catch (Exception ex) {
                String message = OwnerType.getTitle(ownerType);
                message = message + "Произошла ошибка при вызове сервиса проверки по базе решений комиссии о замораживании (блокировании) денежных средств клиента. ";
                message = message + String.format("ФИО: %s %s %s", client.getSurName(), client.getFirstName(), client.getMiddleName());
                execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, message + "Регистрация клиента невозможна " + ex.getMessage());
                return;
            }

            if (!checkResult.isResponseReceived()) {
                String message = OwnerType.getTitle(ownerType);
                message = message + "Не получен ответ при обращении к сервису проверки по базе решений комиссии о замораживании (блокировании) денежных средств клиента.";
                throw new RuntimeException(message);
            }
            PersonnelData blockedPersonnelData = checkResult.getBlockedPersonnelData();
            if (blockedPersonnelData != null) {

                String message = OwnerType.getTitle(ownerType);
                message = message + "Имеется решение комиссии о замораживании (блокировании) денежных средств клиента.";
                message = message + String.format("ФИО: %s %s %s Данные документа. Серия:%s Номер:%s",
                        blockedPersonnelData.getLastName(), blockedPersonnelData.getFirstName(),
                        ensureNotNull(blockedPersonnelData.getMiddleName()),
                        ensureNotNull(blockedPersonnelData.getPassportSeries()),
                        ensureNotNull(blockedPersonnelData.getPassportNumber()));
                execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, message + " Регистрация клиента невозможна");
            }

            Set<Long> checks = (Set<Long>) execution.getVariable(ContextVariablesNames.CHECK_IDS);
            if (checks == null) checks = new HashSet<>();
            checks.add(checkResult.getCheckId());
            execution.setVariable(ContextVariablesNames.CHECK_IDS, checks);
        }
    }

    private String ensureNotNull(String inputString) {
        return StringUtils.isNotBlank(inputString) ? inputString : "";
    }
}
