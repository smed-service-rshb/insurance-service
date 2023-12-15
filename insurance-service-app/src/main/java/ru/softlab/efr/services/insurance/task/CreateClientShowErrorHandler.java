package ru.softlab.efr.services.insurance.task;

import org.springframework.stereotype.Component;
import ru.softlab.efr.clients.model.rest.ShowErrorRequest;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

/**
 * Обработчик задачи отображения ошибки
 *
 * @author basharin
 * @since 03.07.2018
 */
@Component
public class CreateClientShowErrorHandler implements ActionRequestAdapter<ShowErrorRequest> {

    @Override
    public void process(ExecutionContext executionContext, ShowErrorRequest showErrorRequest) throws Exception {
        //executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        executionContext.setVariable(ContextVariablesNames.NEED_EMPLOYEE_ANSWER, false);
        executionContext.setVariable(ContextVariablesNames.DECISION_DATA, null);
    }

}
