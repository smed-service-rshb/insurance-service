package ru.softlab.efr.services.insurance.task;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.repositories.ProgramSettingRepository;
import ru.softlab.efr.services.insurance.services.promocode.PromocodeApiConnectorService;
import ru.softlab.efr.services.insurance.services.promocode.models.PromocodeRegisterModel;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.TM_RESPONSE;
import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.TM_ERROR;

@Component
public class CheckTmServerResponseTask implements JavaDelegate {

    private static final Logger LOGGER = Logger.getLogger(CheckTmServerResponseTask.class);

    private JsonHelper jsonHelper;
    private PromocodeApiConnectorService promocodeApiConnectorService;
    private ProgramSettingRepository programSettingRepository;

    @Autowired
    public CheckTmServerResponseTask(
            JsonHelper jsonHelper,
            PromocodeApiConnectorService promocodeApiConnectorService,
            ProgramSettingRepository programSettingRepository) {
        this.jsonHelper = jsonHelper;
        this.promocodeApiConnectorService = promocodeApiConnectorService;
        this.programSettingRepository = programSettingRepository;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        setDefaultValues(execution);
        SetStatusInsuranceModel model = jsonHelper.deserialize((String) execution.getVariable(ContextVariablesNames.CONTRACT_DATA), SetStatusInsuranceModel.class);

        PromocodeRegisterModel promocodeRegisterModel = new PromocodeRegisterModel();
        promocodeRegisterModel.setActiveStatus("false");
        promocodeRegisterModel.setProductId(programSettingRepository.getOne(model.getProgramSettingId()).getProgram().getNumber());
        promocodeRegisterModel.setProductCode((String) execution.getVariable(ContextVariablesNames.CONTRACT_NUMBER));
        OperationLogEntry operationLog = promocodeApiConnectorService.registerInTm(promocodeRegisterModel);

        if(OperationState.SYSTEM_ERROR.equals(operationLog.getOperationState())) {
            execution.setVariable(TM_ERROR, operationLog.getOperationParameters().get("response"));
        }
        if(OperationState.CLIENT_ERROR.equals(operationLog.getOperationState())) {
            execution.setVariable(TM_ERROR, "Ошибка при подключении к серверу телемедицины");
        }
        if(OperationState.SUCCESS.equals(operationLog.getOperationState())) {
            execution.setVariable(TM_RESPONSE, operationLog.getOperationParameters().get("response"));
        }
    }

    private void setDefaultValues(DelegateExecution execution) {
        execution.setVariable(TM_RESPONSE, null);
        execution.setVariable(TM_ERROR, null);
    }
}
