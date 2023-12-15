package ru.softlab.efr.services.insurance.process;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.softlab.efr.clients.model.IdentityDocType;
import ru.softlab.efr.common.bpm.support.DefaultSubprocessVariablesMapping;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.common.bpm.support.exceptions.BpmException;
import ru.softlab.efr.services.insurance.model.rest.Client;
import ru.softlab.efr.services.insurance.model.rest.Document;
import ru.softlab.efr.services.insurance.model.rest.DocumentType;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.utils.OwnerType;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * Передача данных о страхователе в попроцесс провекрки клиентских данных
 */
@Component
public class CheckHolderSubprocessVariablesMapping extends DefaultSubprocessVariablesMapping {

    private static final Logger LOGGER = Logger.getLogger(CheckHolderSubprocessVariablesMapping.class);

    private JsonHelper jsonHelper;

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    /**
     * Преобразователь
     */
    public CheckHolderSubprocessVariablesMapping() {
        super(set(ContextVariablesNames.CLIENT_ID,  ContextVariablesNames.CLIENT_DATA, ContextVariablesNames.NEED_CHECK_PASSPORT_DATA,
                ContextVariablesNames.OWNER_TYPE, ContextVariablesNames.CHECK_IDS, ContextVariablesNames.EDIT_CONTRACT_MODE/*, ContextVariablesNames.CONTRACT_DATA*/),
                set(ContextVariablesNames.ERROR_MESSAGE, ContextVariablesNames.CHECK_IDS));
    }

    @Override
    public Map<String, Object> mapInputVariables(ExecutionContext executionContext) {
        setDefault(executionContext);
        executionContext.setVariable(ContextVariablesNames.OWNER_TYPE, OwnerType.HOLDER);
        SetStatusInsuranceModel model = jsonHelper.deserialize((String)executionContext.getVariable(ContextVariablesNames.CONTRACT_DATA), SetStatusInsuranceModel.class);
        Client client = model.getHolderData();
        if (client != null) {
            executionContext.setVariable(ContextVariablesNames.CLIENT_DATA, jsonHelper.serialize(client));
            executionContext.setVariable(ContextVariablesNames.CLIENT_ID, model.getHolderId());
            if (client.getDocuments() != null) {
                //Получить паспорт
                Optional<Document> passport = client.getDocuments().stream().filter(d -> DocumentType.PASSPORT_RF.equals(d.getDocType())).findFirst();
                if (passport.isPresent()) {
                    executionContext.setVariable(ContextVariablesNames.NEED_CHECK_PASSPORT_DATA, true);
                }
            }
        } else {
            throw new BpmException("Не определены данные застрахованного лица !");
        }
        return super.mapInputVariables(executionContext);
    }

    private void setDefault(ExecutionContext executionContext) {
        executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        executionContext.setVariable(ContextVariablesNames.CLIENT_DATA, null);
        executionContext.setVariable(ContextVariablesNames.CLIENT_ID, null);
        executionContext.setVariable(ContextVariablesNames.NEED_CHECK_PASSPORT_DATA, false);
        executionContext.setVariable(ContextVariablesNames.HOLDER_CHECK_IDS, new HashSet<>());
        executionContext.setVariable(ContextVariablesNames.CHECK_IDS, new HashSet<>());
    }

    @Override
    public void mapOutputVariables(ExecutionContext executionContext, Map<String, Object> subProcessVariables) {
        super.mapOutputVariables(executionContext, subProcessVariables);
        if (subProcessVariables.get(ContextVariablesNames.CLIENT_ID) != null) {
            Long clientId = (Long) subProcessVariables.get(ContextVariablesNames.CLIENT_ID);
            SetStatusInsuranceModel model = jsonHelper.deserialize((String)executionContext.getVariable(ContextVariablesNames.CONTRACT_DATA), SetStatusInsuranceModel.class);
            Client client = model.getHolderData();
            client.setId(clientId.toString());
            model.setHolderId(clientId);
            executionContext.setVariable(ContextVariablesNames.CONTRACT_DATA, jsonHelper.serialize(model));
        }
        if (subProcessVariables.get(ContextVariablesNames.CHECK_IDS) != null) {
            executionContext.setVariable(ContextVariablesNames.HOLDER_CHECK_IDS, subProcessVariables.get(ContextVariablesNames.CHECK_IDS));
        }
    }
}
