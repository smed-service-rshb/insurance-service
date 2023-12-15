package ru.softlab.efr.services.insurance.task;

import ru.softlab.efr.clients.model.rest.EditClientRequest;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

public class CreateContractTask implements ActionRequestAdapter<SetStatusInsuranceModel> {

    @Override
    public void process(ExecutionContext executionContext, SetStatusInsuranceModel insuranceModel) throws Exception {
        executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        executionContext.setVariable(ContextVariablesNames.CLIENT_DATA, null);
        executionContext.setVariable(ContextVariablesNames.CLIENT_ID, null);
        executionContext.setVariable(ContextVariablesNames.NEED_CHECK_PASSPORT_DATA, false);
    }

}
