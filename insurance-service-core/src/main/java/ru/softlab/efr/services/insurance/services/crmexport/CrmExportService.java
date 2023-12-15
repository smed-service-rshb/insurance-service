package ru.softlab.efr.services.insurance.services.crmexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.repositories.InsuranceRepository;
import ru.softlab.efr.services.insurance.services.crmexport.models.CrmReportModel;

import java.util.Collections;
import java.util.List;

@Service
public class CrmExportService {

    private InsuranceRepository insuranceRepository;
    private GetCrmReportByInsuranceList getCrmReportByInsuranceList;
    private CsvReportService csvReportService;

    @Autowired
    public CrmExportService(InsuranceRepository insuranceRepository,
                            GetCrmReportByInsuranceList getCrmReportByInsuranceList,
                            CsvReportService csvReportService) {
        this.insuranceRepository = insuranceRepository;
        this.getCrmReportByInsuranceList = getCrmReportByInsuranceList;
        this.csvReportService = csvReportService;
    }

    public void exportMadeInsurancesToCrmFolder() {
        List<Insurance> madeInsurances = insuranceRepository.getByStatusAndDeleted(InsuranceStatusCode.MADE, false);
        List<CrmReportModel> reportModels = getCrmReportByInsuranceList.getReportByInsuranceList(madeInsurances);
        csvReportService.createReport(reportModels);
    }
}
