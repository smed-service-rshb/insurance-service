package ru.softlab.efr.services.insurance.services.crmexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.crmexport.models.CrmReportModel;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetCrmReportByInsuranceList {

    private InsuranceService insuranceService;
    private CrmReportConverter crmReportConverter;

    @Autowired
    public GetCrmReportByInsuranceList(InsuranceService insuranceService,
                                       CrmReportConverter crmReportConverter) {
        this.insuranceService = insuranceService;
        this.crmReportConverter = crmReportConverter;
    }

    public List<CrmReportModel> getReportByInsuranceList(List<Insurance> insurances) {
        List<CrmReportModel> responseList = new ArrayList<>();
        insurances.forEach(insurance -> {
            responseList.add(crmReportConverter.getReportByInsurance(insurance));
            insuranceService.setStatus(
                    insurance,
                    InsuranceStatusCode.CRM_IMPORTED,
                    insurance.getBranchName(), insurance.getEmployeeId(), insurance.getEmployeeName(), insurance.getSubdivisionId(), insurance.getSubdivisionName(),
                    "Автоматический экспорт в CRM для системы СМС", null);
        });
        return responseList;
    }
}
