package ru.softlab.efr.services.insurance.pojo;


import ru.softlab.efr.services.insurance.model.db.Insurance;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class InsuranceParseResult {
    private List<Insurance> parsedInsurances = new ArrayList<>();
    private List<AbstractMap.SimpleEntry<Long, String>> errors = new ArrayList<>();

    public List<Insurance> getParsedInsurances() {
        return parsedInsurances;
    }

    public void setParsedInsurances(List<Insurance> parsedInsurances) {
        this.parsedInsurances = parsedInsurances;
    }

    public List<AbstractMap.SimpleEntry<Long, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<AbstractMap.SimpleEntry<Long, String>> errors) {
        this.errors = errors;
    }
}
