package ru.softlab.efr.services.insurance.task;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softlab.efr.clients.model.Client;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.pojo.CheckResult;
import ru.softlab.efr.services.insurance.services.CheckService;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.HashSet;
import java.util.Set;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.CLIENT_DATA;

/**
 * Обработчик проверки клиента по справочнику экстремистов
 *
 * @author Krivenko
 * @since 20.09.2018
 */
@Component
public class CheckTerroristTask implements JavaDelegate {

    private final static String DOCUMENT_CHECK_SETTINGS = "documentDataChecks";
    private static final Logger LOGGER = Logger.getLogger(CheckTerroristTask.class);

    @Autowired
    private CheckService checkService;

    @Autowired
    private SettingsService settingsService;

    private JsonHelper jsonHelper;

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
            Client client = jsonHelper.deserialize((String) execution.getVariable(CLIENT_DATA), Client.class);

            try {
                CheckResult checkResult = checkService.checkClientByTerrorist(client);
                if (!checkResult.isResponseReceived()) {
                    String message = OwnerType.getTitle(ownerType);
                    message = message + "Произошла ошибка при вызове сервиса проверки по базе данных экстремистов/террористов.";
                    execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, message + " Регистрация клиента невозможна");
                    //execution.setVariable(ContextVariablesNames.DECISION_DATA, jsonHelper.serialize(DecisionRequest.getContinueRequest(message)));
                    return;
                }
                if (!checkResult.isCheckSuccess()) {
                    String message = OwnerType.getTitle(ownerType);
                    message = message + String.format("Клиент ФИО %s %s%s не прошел проверку по справочнику экстремистов/террористов.",
                            client.getSurName(), client.getFirstName(), ensureNotNull(client.getMiddleName()));
                    execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, message + " Регистрация клиента невозможна.");
                }
                Set<Long> checks = (Set<Long>) execution.getVariable(ContextVariablesNames.CHECK_IDS);
                if (checks == null) checks = new HashSet<>();
                checks.add(checkResult.getCheckId());
                execution.setVariable(ContextVariablesNames.CHECK_IDS, checks);
            } catch (Exception e) {
                LOGGER.error(e);
                String message = OwnerType.getTitle(ownerType);
                message = message + "Произошла ошибка при вызове сервиса проверки по базе данных экстремистов/террористов. ";
                execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, message + "Регистрация клиента невозможна " + e.getMessage());
                //execution.setVariable(ContextVariablesNames.DECISION_DATA, jsonHelper.serialize(DecisionRequest.getContinueRequest(message)));
            }
        }
    }

    private String ensureNotNull(String inputString) {
        return StringUtils.isNotBlank(inputString) ? " ".concat(inputString) : "";
    }
}
