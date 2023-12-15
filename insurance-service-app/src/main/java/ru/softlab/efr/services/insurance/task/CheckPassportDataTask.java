package ru.softlab.efr.services.insurance.task;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softlab.efr.clients.model.Client;
import ru.softlab.efr.clients.model.Document;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.pojo.CheckResult;
import ru.softlab.efr.services.insurance.services.CheckService;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.CLIENT_DATA;

/**
 * Обработчик проверки на паспорта на наличие в реестре недействительных паспортов
 *
 * @author Krivenko
 * @since 20.09.2018
 */
@Component
public class CheckPassportDataTask implements JavaDelegate {

    private final static String DOCUMENT_CHECK_SETTINGS = "documentDataChecks";
    private static final Logger LOGGER = Logger.getLogger(CheckPassportDataTask.class);

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private CheckService checkService;

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
            //Получить паспорт
            Optional<Document> identityDoc = client.getDocuments().stream().filter(d -> IdentityDocTypeEnum.PASSPORT_RF.name().equals(d.getDocType())).findFirst();
            if (!identityDoc.isPresent()) {
                return;
            }
            Document passport = identityDoc.get();
            try {
                CheckResult checkResult = checkService.checkClientByPassport(client);
                if (!checkResult.isResponseReceived()) {
                    String msg = OwnerType.getTitle(ownerType);
                    msg = msg + "Произошла ошибка при вызове сервиса проверки данных паспорта. Сервис проверки не доступен.";
                    execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, msg);
                    //execution.setVariable(ContextVariablesNames.DECISION_DATA, jsonHelper.serialize(DecisionRequest.getContinueRequest(msg)));
                    return;
                }
                if (!checkResult.isCheckSuccess()) {
                    String msg = OwnerType.getTitle(ownerType);
                    msg = msg + String.format("Паспорт %s %s клиента недействующий. Регистрация клиента невозможна", passport.getDocSeries(), passport.getDocNumber());
                    execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, msg);
                    //execution.setVariable(ContextVariablesNames.DECISION_DATA, jsonHelper.serialize(DecisionRequest.getContinueRequest(msg)));
                }

                Set<Long> checks = (Set<Long>) execution.getVariable(ContextVariablesNames.CHECK_IDS);
                if (checks == null) checks = new HashSet<>();
                checks.add(checkResult.getCheckId());
                execution.setVariable(ContextVariablesNames.CHECK_IDS, checks);
            } catch (Exception e) {
                LOGGER.error(e);
                String msg = OwnerType.getTitle(ownerType);
                msg = msg + "Произошла ошибка при вызове сервиса проверки данных паспорта " + e.getMessage();
                execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, msg);
                //execution.setVariable(ContextVariablesNames.DECISION_DATA, jsonHelper.serialize(DecisionRequest.getContinueRequest(msg)));
            }
        }
    }
}
