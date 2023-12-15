package ru.softlab.efr.services.insurance.task;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.clients.model.rest.DecisionOption;
import ru.softlab.efr.clients.model.rest.DecisionRequest;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.rest.Client;
import ru.softlab.efr.services.insurance.model.rest.FoundClient;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.services.ClientService;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.List;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.*;

@Component
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class CheckDuplicateClientTask implements JavaDelegate {

    private static final Logger LOGGER = Logger.getLogger(CheckDuplicateClientTask.class);
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

    @Value("${client.duplicateClient.error}")
    private String clientDuplicateErrorMessage;

    @Value("${client.duplicatePhone.error}")
    private String phoneDuplicateErrorMessage;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        //setDefaultValues(execution);
        Long clientId = (Long) execution.getVariable(CLIENT_ID);
        //if (clientId != null) return;  //Клиент определен
        Client client = jsonHelper.deserialize((String) execution.getVariable(CLIENT_DATA), Client.class);

        long clientCountWithSameData = clientService.countClient(client.getSurName(), client.getFirstName(), client.getMiddleName(), client.getBirthDate(), ClientEntity.getMobilePhone(client), clientId);
        if (clientCountWithSameData > 0) {
            execution.setVariable(ContextVariablesNames.FOUND_DUPLICATE_CLIENT, Boolean.TRUE);
            execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, clientDuplicateErrorMessage);
            return;
        }

        long clientCountWithSameMobilePhone = clientService.countClient(null, null, null, null,  ClientEntity.getMobilePhone(client), clientId);
        if (clientCountWithSameMobilePhone > 0) {
            execution.setVariable(ContextVariablesNames.FOUND_DUPLICATE_CLIENT, Boolean.TRUE);
            execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, phoneDuplicateErrorMessage);
            return;
        }

        execution.setVariable(ContextVariablesNames.FOUND_DUPLICATE_CLIENT, Boolean.FALSE);
        execution.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
    }
}
