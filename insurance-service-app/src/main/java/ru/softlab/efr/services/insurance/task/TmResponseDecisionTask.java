package ru.softlab.efr.services.insurance.task;

import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.model.rest.ClientDecisionSmsServiceEnum;
import ru.softlab.efr.services.insurance.model.rest.ClientDecisionSmsServiceResponse;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

/**
 * Обработчик ответа на запрос
 */
public class TmResponseDecisionTask implements ActionRequestAdapter<ClientDecisionSmsServiceResponse> {
    @Override
    public void process(ExecutionContext executionContext, ClientDecisionSmsServiceResponse decision) throws Exception {
        if (ClientDecisionSmsServiceEnum.IGNORE.equals(decision.getValue())) {
            executionContext.setVariable(ContextVariablesNames.TM_ERROR, null);
            executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        } else {
            /*todo: доработка статуса до работы с потомами, сменить сообщение на: Информация об оплате поступит в систему в течение 24 часов*/
            executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, "Повторите попытку отправки в течении 24 часов ");
        }
    }
}