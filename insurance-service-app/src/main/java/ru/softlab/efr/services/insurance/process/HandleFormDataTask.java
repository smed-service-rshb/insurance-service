package ru.softlab.efr.services.insurance.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.common.bpm.support.ActionRequestAdapter;
import ru.softlab.efr.common.bpm.support.ExecutionContext;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.insurance.converter.InsuranceConverter;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.BaseInsuranceModel;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.model.utils.JsonHelper;
import ru.softlab.efr.services.insurance.services.InsuranceCheckService;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.ProgramSettingService;
import ru.softlab.efr.services.insurance.services.contructnumbers.ContractNumberSequenceApi;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandleFormDataTask implements ActionRequestAdapter<SetStatusInsuranceModel> {

    private JsonHelper jsonHelper;

    private PrincipalDataSource principalDataSource;

    private InsuranceConverter insuranceConverter;

    private InsuranceCheckService insuranceCheckService;

    private InsuranceService insuranceService;

    private ProgramSettingService programSettingService;

    @Value("${insurance.converter.static.date.notvalid}")
    private String STATIC_DATE_LESS_CURRENT_DATE;

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

    @Autowired
    public void setProgramSettingService(ProgramSettingService programSettingService) {
        this.programSettingService = programSettingService;
    }

    @Autowired
    private ContractNumberSequenceApi contractNumberSequenceApi;

    @Override
    public void process(ExecutionContext executionContext, SetStatusInsuranceModel statusModel) throws Exception {
        eraseVars(executionContext);
        executionContext.setVariable(ContextVariablesNames.CONTRACT_DATA, jsonHelper.serialize(statusModel));
        executionContext.setVariable(ContextVariablesNames.PRINCIPAL_DATA, jsonHelper.serialize(principalDataSource.getPrincipalData()));
        Boolean holderEqualsInsured = statusModel.isHolderEqualsInsured() != null ? statusModel.isHolderEqualsInsured() : Boolean.TRUE;
        executionContext.setVariable(ContextVariablesNames.HOLDER_EQUALS_INSURED, holderEqualsInsured);
        executionContext.setVariable(ContextVariablesNames.PROGRAM_KIND, statusModel.getKind().name());
        if (statusModel.getNewStatus() != null) {
            List<String> errorsMsg = new ArrayList<>();
            executionContext.setVariable(ContextVariablesNames.TARGET_STATUS, InsuranceStatusCode.valueOf(statusModel.getNewStatus().name()));
            Long id = statusModel.getId();
            Insurance insurance = null;
            if (id != null) {
                insurance = insuranceService.findById(id);
                executionContext.setVariable(ContextVariablesNames.EDIT_CONTRACT_MODE, Boolean.TRUE);
            } else {
                insurance = new Insurance();
                executionContext.setVariable(ContextVariablesNames.EDIT_CONTRACT_MODE, Boolean.FALSE);
            }
            if (insurance == null) {
                errorsMsg.add("Не удается найти объект с id=" + id);
                throw new ValidationException(errorsMsg);
            }
            Insurance insuranceToValidate = new Insurance();
            BaseInsuranceModel baseInsuranceModel = insuranceConverter.convertSetStatusInsuranceModelToBaseInsuranceModel(statusModel);
            ProgramSetting programSetting = programSettingService.findById(baseInsuranceModel.getProgramSettingId());
            if (programSetting != null && programSetting.getStaticDate() != null
                    && programSetting.getStaticDate().compareTo(new Date()) < 0) {
                errorsMsg.add(STATIC_DATE_LESS_CURRENT_DATE);
                throw new ValidationException(errorsMsg);
            }
            insuranceConverter.updateInsuranceFromBaseModel(baseInsuranceModel, insuranceToValidate, null, null, null, null);
            List<CheckModel> errors = insuranceCheckService.getContractErrors(baseInsuranceModel, insuranceToValidate, statusModel.getNewStatus().name());
            if (!CollectionUtils.isEmpty(errors)) {
                throw new ValidationException(errors.stream().map(CheckModel::getValue).collect(Collectors.toList()));
            }
            if (insurance.getContractNumber() == null) {
                executionContext.setVariable(ContextVariablesNames.CONTRACT_NUMBER, contractNumberSequenceApi.generateContractNumber(insuranceToValidate));
            } else {
                executionContext.setVariable(ContextVariablesNames.CONTRACT_NUMBER, insurance.getContractNumber());
            }
        }

    }

    private void eraseVars(ExecutionContext executionContext) {
        executionContext.setVariable(ContextVariablesNames.CONTRACT_NUMBER, null);
        executionContext.setVariable(ContextVariablesNames.ERROR_MESSAGE, null);
        executionContext.setVariable(ContextVariablesNames.INSURANCE_DECISION_DATA, null);
        executionContext.setVariable(ContextVariablesNames.EMPLOYEE_DECISION, null);
        executionContext.setVariable(ContextVariablesNames.PROGRAM_KIND, null);
        executionContext.setVariable(ContextVariablesNames.FIXED_DATE_START, null);
        executionContext.setVariable(ContextVariablesNames.HOLDER_CHECK_IDS, new HashSet<>());
        executionContext.setVariable(ContextVariablesNames.INSURED_CHECK_IDS, new HashSet<>());
    }

}
