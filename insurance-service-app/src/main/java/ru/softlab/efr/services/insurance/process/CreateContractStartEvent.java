package ru.softlab.efr.services.insurance.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.services.InsuranceCheckService;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateContractStartEvent implements ActionRequestAdapter<SetStatusInsuranceModel> {

    private JsonHelper jsonHelper;

    private PrincipalDataSource principalDataSource;

    private InsuranceConverter insuranceConverter;

    private InsuranceCheckService insuranceCheckService;

    private InsuranceService insuranceService;

    @Autowired
    public void setInsuranceConverter(InsuranceConverter insuranceConverter) {
        this.insuranceConverter = insuranceConverter;
    }

    @Autowired
    public void setInsuranceCheckService(InsuranceCheckService insuranceCheckService) {
        this.insuranceCheckService = insuranceCheckService;
    }

    @Autowired
    public void setInsuranceService(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Autowired
    public void setJsonHelper(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    @Autowired
    public void setPrincipalDataSource(PrincipalDataSource principalDataSource) {
        this.principalDataSource = principalDataSource;
    }

    @Override
    public void process(ExecutionContext executionContext, SetStatusInsuranceModel statusModel) throws Exception {
        executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        executionContext.setVariable(ContextVariablesNames.CONTRACT_DATA, jsonHelper.serialize(statusModel));
        executionContext.setVariable(ContextVariablesNames.PRINCIPAL_DATA, jsonHelper.serialize(principalDataSource.getPrincipalData()));
        Boolean holderEqualsInsured = statusModel.isHolderEqualsInsured() != null ? statusModel.isHolderEqualsInsured() : Boolean.TRUE;
        executionContext.setVariable(ContextVariablesNames.HOLDER_EQUALS_INSURED, holderEqualsInsured);
    }
}
