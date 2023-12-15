package ru.softlab.efr.services.insurance.task;

import org.springframework.stereotype.Component;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

//TODO сделать операцию терминальной, сейчас она по ДО идет вновь на проверку дублей,
// но на практике это последняя операция. После неё ДО стартует по новому
@Component
public class NotifyDuplicateClientTask implements ActionRequestAdapter {

    @Override
    public void process(ExecutionContext executionContext, Object o) throws Exception {
        executionContext.setVariable(ContextVariablesNames.FOUND_DUPLICATE_CLIENT, Boolean.FALSE);
        executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
    }
}
