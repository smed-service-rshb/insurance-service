package ru.softlab.efr.services.insurance.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.OrgUnitAuthServiceClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.GetOrgUnitFullRs;
import ru.softlab.efr.services.auth.orgunit.OfficeData;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.insurance.utils.AppUtils;
import ru.softlab.efr.services.insurance.utils.EmployeeFilter;

import java.util.*;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.services.InsuranceAvailabilityService.isAdmin;

@Service
public class EmployeeFilterService {

    private static final long GET_ORG_UNIT_TIMEOUT = 10000L;
    private static final Long UNEXIST_EMPLOYEE_ID = -1L;

    private final OrgUnitAuthServiceClient orgUnitClient;

    @Autowired
    public EmployeeFilterService(OrgUnitAuthServiceClient orgUnitClient) {
        this.orgUnitClient = orgUnitClient;
    }

    public EmployeeFilter getFilterForPrincipal(PrincipalData principalData) throws Exception {
        List<Right> rights = principalData.getRights();

        Set<Long> employeeOfficesFilter = new HashSet<>();
        List<String> employeeGroupFilter = CollectionUtils.isEmpty(principalData.getGroups()) ? new ArrayList<>() :
                principalData.getGroups();
        Long employeeIdFilter = UNEXIST_EMPLOYEE_ID;
        if (rights.contains(Right.VIEW_CONTRACT_LIST_RF_VSP)) {
            // У пользователя есть права на просмотр всех договоров, оформленных в региональном филиале,
            // к которому он прикреплён. Соответственно, определим идентификаторы всех ВСП, к которым пользователь имеет доступ.
            Set<Long> offices = getUserBranchesOfficeIds(principalData);
            employeeOfficesFilter.addAll(offices);
        }
        if (rights.contains(Right.VIEW_CONTRACT_LIST_VSP)) {
            // У пользователя есть права на просмотр всех договоров ВСП, поэтому добавляем возможность
            // фильтрации по идентификатору ВСП.
            employeeOfficesFilter.add(principalData.getOfficeId());
        }
        if (rights.contains(Right.VIEW_CONTRACT_LIST_OWNER)) {
            // У пользователя есть права на просмотр договоров, которые он создал, поэтому добавляем позможность
            // фильтрации по идентификатору пользователя.
            employeeIdFilter = principalData.getId();
        }
        return new EmployeeFilter(rights.contains(Right.VIEW_CONTRACT_LIST_ALL),
                isAdmin(principalData),
                employeeIdFilter,
                employeeOfficesFilter,
                employeeGroupFilter);

    }

    private Set<Long> getUserBranchesOfficeIds(PrincipalData principalData) throws Exception {
        GetOrgUnitFullRs orgUnitList = orgUnitClient.getOrgUnitList(GET_ORG_UNIT_TIMEOUT);

        if (principalData == null || orgUnitList== null ||
                CollectionUtils.isEmpty(principalData.getOffices()) || CollectionUtils.isEmpty(orgUnitList.getOrgUnits())) {
            return new HashSet<>();
        }

        return orgUnitList.getOrgUnits()
                .stream()
                .filter(branch -> branch.getOffices()
                        .stream()
                        .anyMatch(officeData -> principalData.getOffices().contains(officeData.getName())))
                .flatMap(branch -> branch.getOffices().stream())
                .map(OfficeData::getOfficeId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<Long> getOfficeIdsByNames(List<String> officeNameList) throws Exception {
        GetOrgUnitFullRs orgUnitList = orgUnitClient.getOrgUnitList(GET_ORG_UNIT_TIMEOUT);
        if (orgUnitList == null || officeNameList == null || CollectionUtils.isEmpty(officeNameList) || CollectionUtils.isEmpty(orgUnitList.getOrgUnits())) {
            return new HashSet<>();
        }

        return orgUnitList.getOrgUnits()
                .stream()
                .flatMap(branch -> branch.getOffices().stream())
                .filter(officeData -> officeNameList.contains(officeData.getName()))
                .map(OfficeData::getOfficeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public List<String> getOfficeNamesByIds(List<Long> officeIds) throws Exception {
        GetOrgUnitFullRs orgUnitList = orgUnitClient.getOrgUnitList(GET_ORG_UNIT_TIMEOUT);
        if (CollectionUtils.isEmpty(officeIds) || CollectionUtils.isEmpty(orgUnitList.getOrgUnits())) {
            return new ArrayList<>();
        }
        return orgUnitList.getOrgUnits()
                .stream()
                .flatMap(branch -> branch.getOffices().stream())
                .filter(officeData -> officeIds.contains(officeData.getOfficeId()))
                .map(OfficeData::getName)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
