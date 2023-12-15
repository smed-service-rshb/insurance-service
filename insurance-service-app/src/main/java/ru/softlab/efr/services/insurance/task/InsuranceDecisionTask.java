package ru.softlab.efr.services.insurance.task;

import ru.softlab.efr.clients.model.rest.DecisionAnswerRequest;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

/**
 * Обработчик ответа на запрос
 */
public class InsuranceDecisionTask implements ActionRequestAdapter<DecisionAnswerRequest> {
    @Override
    public void process(ExecutionContext executionContext, DecisionAnswerRequest decisionAnswerRequest) throws Exception {

    }
}
