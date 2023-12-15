package ru.softlab.efr.services.insurance.task;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.rest.DecisionOption;
import ru.softlab.efr.clients.model.rest.DecisionRequest;
import ru.softlab.efr.services.insurance.model.rest.Client;
import ru.softlab.efr.services.insurance.model.rest.FoundClient;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.List;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.*;


/**
 * Задача поиска клиента в БД
 */
@Component
public class ClientSearchTask implements JavaDelegate {

    private static final Logger LOGGER = Logger.getLogger(ClientSearchTask.class);
    private JsonHelper jsonHelper;
    private ClientService clientService;

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        setDefaultValues(execution);
        Long clientId = (Long) execution.getVariable(CLIENT_ID);
        if (clientId != null) return;  //Клиент определен
        OwnerType ownerType = (OwnerType) execution.getVariable(ContextVariablesNames.OWNER_TYPE);
        Client client = jsonHelper.deserialize((String) execution.getVariable(CLIENT_DATA), Client.class);
        List<FoundClient> clients = clientService.findSimilarClients(client);
        if (CollectionUtils.isEmpty(clients)) return;  //Похожих клиентов не найдено
        //Найдены клиенты с указанными данными
        execution.setVariable(NEED_EMPLOYEE_ANSWER, true);
        String msg = String.format("Найдены клиенты с данными схожими с анкетой %s.", ownerType != null ? ownerType == OwnerType.HOLDER ? "страхователя" : "застрахованного" : "клиента");
        DecisionRequest decisionRequest = new DecisionRequest(msg, DecisionOption.CONTINUE, DecisionOption.CANCEL);
        execution.setVariable(DECISION_DATA, jsonHelper.serialize(decisionRequest));
        execution.setVariable(CLIENT_DECISION_DATA, jsonHelper.serialize(clients));
    }

    private void setDefaultValues(DelegateExecution execution) {
        execution.setVariable(NEED_EMPLOYEE_ANSWER, false);
        execution.setVariable(DECISION_DATA, null);
        execution.setVariable(CLIENT_DECISION_DATA, null);
    }
}
