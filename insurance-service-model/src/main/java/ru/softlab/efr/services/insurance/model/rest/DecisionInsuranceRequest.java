package ru.softlab.efr.services.insurance.model.rest;

import ru.softlab.efr.clients.model.rest.DecisionOption;
import ru.softlab.efr.clients.model.rest.DecisionRequest;

import java.util.ArrayList;
import java.util.List;

public class DecisionInsuranceRequest extends DecisionRequest {

    public DecisionInsuranceRequest() {
        super();
    }

    public DecisionInsuranceRequest(String message, DecisionOption... options) {
        super(message, options);
    }

    /**
     * Список найденных договоров
     */
    private List<FoundInsurance> insuranceList = new ArrayList<>();

    public List<FoundInsurance> getInsuranceList() {
        return insuranceList;
    }

    public void setInsuranceList(List<FoundInsurance> insuranceList) {
        this.insuranceList = insuranceList;
    }
}
