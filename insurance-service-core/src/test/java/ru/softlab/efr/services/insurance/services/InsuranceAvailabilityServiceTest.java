package ru.softlab.efr.services.insurance.services;

import org.junit.Test;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.enums.RelatedEmployeeGroupFilterType;
import ru.softlab.efr.services.insurance.model.enums.RelatedOfficeFilterType;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author olshansky
 * @since 25.02.2019
 */
public class InsuranceAvailabilityServiceTest {

    InsuranceAvailabilityService insuranceAvailabilityService = new InsuranceAvailabilityService();

    @Test
    public void getContractErrors() {

        for (InsuranceStatusCode statusCode : InsuranceStatusCode.values()) {

            assertTrue(isAllowed(insuranceAvailabilityService.getContractErrors(getAvailInsuranceContract(), statusCode.name(), getAllowedUser())));

            if (InsuranceStatusCode.MADE.equals(statusCode) || InsuranceStatusCode.MADE_NOT_COMPLETED.equals(statusCode)) {
                assertTrue(!isAllowed(insuranceAvailabilityService.getContractErrors(getNotAvailInsuranceContract(), statusCode.name(), getAllowedUser())));
                assertTrue(!isAllowed(insuranceAvailabilityService.getContractErrors(getRestrictedByGroupInsuranceContract(), statusCode.name(), getRestrictedUser())));
                assertTrue(!isAllowed(insuranceAvailabilityService.getContractErrors(getRestrictedByOrgunitInsuranceContract(), statusCode.name(), getRestrictedUser())));
            } else {
                assertTrue(isAllowed(insuranceAvailabilityService.getContractErrors(getNotAvailInsuranceContract(), statusCode.name(), getAllowedUser())));
                assertTrue(isAllowed(insuranceAvailabilityService.getContractErrors(getRestrictedByGroupInsuranceContract(), statusCode.name(), getRestrictedUser())));
                assertTrue(isAllowed(insuranceAvailabilityService.getContractErrors(getRestrictedByOrgunitInsuranceContract(), statusCode.name(), getRestrictedUser())));
            }
        }
    }

    @Test
    public void isProgramAllowable() {
        assertTrue(InsuranceAvailabilityService.isProgramAllowableByOrgUnit(getAvailProgram(), getAllowedUser()));

        assertTrue(InsuranceAvailabilityService.isProgramAllowableByOrgUnit(
                getRestrictedByOrgunitInsuranceContract().getProgramSetting().getProgram(), getAllowedUser()));

        assertTrue(!InsuranceAvailabilityService.isProgramAllowableByOrgUnit(
                getRestrictedByOrgunitInsuranceContract().getProgramSetting().getProgram(), getRestrictedUser()));

        assertTrue(InsuranceAvailabilityService.isProgramAllowableByGroup(getAvailProgram(), getAllowedUser()));
        assertTrue(InsuranceAvailabilityService.isProgramAllowableByGroup(
                getRestrictedByGroupInsuranceContract().getProgramSetting().getProgram(), getAllowedUser()));

        assertTrue(!InsuranceAvailabilityService.isProgramAllowableByGroup(
                getRestrictedByGroupInsuranceContract().getProgramSetting().getProgram(), getRestrictedUser()));
    }

    @Test
    public void isProgramAllowableByOrgUnit() {
        assertTrue(InsuranceAvailabilityService.isProgramAllowableByOrgUnit(getAvailProgram(), getAllowedUser()));

        assertTrue(InsuranceAvailabilityService.isProgramAllowableByOrgUnit(
                getRestrictedByOrgunitInsuranceContract().getProgramSetting().getProgram(), getAllowedUser()));

        assertTrue(!InsuranceAvailabilityService.isProgramAllowableByOrgUnit(
                getRestrictedByOrgunitInsuranceContract().getProgramSetting().getProgram(), getRestrictedUser()));
    }

    @Test
    public void isProgramAllowableByGroup() {
        assertTrue(InsuranceAvailabilityService.isProgramAllowableByGroup(getAvailProgram(), getAllowedUser()));

        assertTrue(InsuranceAvailabilityService.isProgramAllowableByGroup(
                getRestrictedByGroupInsuranceContract().getProgramSetting().getProgram(), getAllowedUser()));

        assertTrue(!InsuranceAvailabilityService.isProgramAllowableByGroup(
                getRestrictedByGroupInsuranceContract().getProgramSetting().getProgram(), getRestrictedUser()));
    }

    private boolean isAllowed(List<CheckModel> errors) {
        return CollectionUtils.isEmpty(errors);
    }

    private Insurance getRestrictedByGroupInsuranceContract() {
        Insurance insuranceContract = getAvailInsuranceContract();
        insuranceContract.getProgramSetting().getProgram().setRelatedEmployeeGroupFilterType(RelatedEmployeeGroupFilterType.EXCLUDE);
        insuranceContract.getProgramSetting().getProgram().setRelatedEmployeeGroups(
                Collections.singletonList(new RelatedEmployeeGroup(null, "RESTRICTED GROUP")));
        return insuranceContract;
    }

    private Insurance getRestrictedByOrgunitInsuranceContract() {
        Insurance insuranceContract = getAvailInsuranceContract();
        insuranceContract.getProgramSetting().getProgram().setRelatedOfficeFilterType(RelatedOfficeFilterType.EXCLUDE);
        insuranceContract.getProgramSetting().getProgram().setRelatedOffices(
                Collections.singletonList(new RelatedOffice(null, 403L)));
        return insuranceContract;
    }

    private Insurance getAvailInsuranceContract() {
        Insurance insuranceContract = new Insurance();
        insuranceContract.setProgramSetting(getAvailProgramSetting());
        return insuranceContract;
    }

    private Insurance getNotAvailInsuranceContract() {
        Insurance insuranceContract = new Insurance();
        insuranceContract.setProgramSetting(getNotAvailProgramSetting());
        return insuranceContract;
    }

    private ProgramSetting getAvailProgramSetting() {
        ProgramSetting programSetting = new ProgramSetting();
        programSetting.setDeleted(false);
        programSetting.setEndDate(null);
        programSetting.setProgram(getAvailProgram());
        return programSetting;
    }

    private ProgramSetting getNotAvailProgramSetting() {
        ProgramSetting programSetting = new ProgramSetting();
        programSetting.setDeleted(false);
        programSetting.setEndDate(Timestamp.valueOf(LocalDate.of(2009, 1, 1).atStartOfDay()));
        programSetting.setProgram(getNotAvailProgram());
        return programSetting;
    }

    private Program getAvailProgram() {
        Program program = new Program();
        program.setActive(true);
        program.setDeleted(false);
        program.setRelatedEmployeeGroups(new ArrayList<>());
        program.setRelatedOffices(new ArrayList<>());
        return program;
    }

    private Program getNotAvailProgram() {
        Program program = new Program();
        program.setActive(false);
        program.setDeleted(false);
        program.setRelatedEmployeeGroups(new ArrayList<>());
        program.setRelatedOffices(new ArrayList<>());
        return program;
    }

    private PrincipalDataImpl getAllowedUser() {
        PrincipalDataImpl allowedUser = new PrincipalDataImpl();
        allowedUser.setGroups(Collections.singletonList("ALLOWED GROUP"));
        allowedUser.setOffice("ALLOWED ORGUNIT");
        allowedUser.setOfficeId(200L);
        return allowedUser;
    }

    private PrincipalDataImpl getRestrictedUser() {
        PrincipalDataImpl allowedUser = new PrincipalDataImpl();
        allowedUser.setGroups(Collections.singletonList("RESTRICTED GROUP"));
        allowedUser.setOffice("RESTRICTED ORGUNIT");
        allowedUser.setOfficeId(403L);
        return allowedUser;
    }
}