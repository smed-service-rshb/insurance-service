package ru.softlab.efr.services.insurance.task;

import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.model.rest.ClientDecisionResponse;

import static ru.softlab.efr.services.insurance.validation.ContextVariablesNames.*;

/**
 * Обработчик ответа на запрос о выборе клиента из списка
 */
public class ClientDecisionTask implements ActionRequestAdapter<ClientDecisionResponse> {
    @Override
    public void process(ExecutionContext executionContext, ClientDecisionResponse response) throws Exception {
        executionContext.setVariable(NEED_EMPLOYEE_ANSWER, false);
        executionContext.setVariable(DECISION_DATA, null);
        executionContext.setVariable(CLIENT_DECISION_DATA, null);
        if (response.isClientAnswered() != null && response.isClientAnswered()) {
            executionContext.setVariable(CLIENT_ID, response.getClientAnswerId());
        }
    }
}
