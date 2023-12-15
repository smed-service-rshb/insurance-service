package ru.softlab.efr.services.insurance.services.crmexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.services.crmexport.models.CrmReportModel;

import java.util.Collections;
import java.util.List;

@Service
public class CrmExportStatusService {

    private CsvReportService csvReportService;
    private CrmReportConverter crmReportConverter;

    @Autowired
    public CrmExportStatusService(CsvReportService csvReportService,
                                  CrmReportConverter crmReportConverter) {
        this.csvReportService = csvReportService;
        this.crmReportConverter = crmReportConverter;
    }

    public void exportInsuranceByChangeStatus(Insurance insurance) {
        List<CrmReportModel> reportModels = Collections.singletonList(crmReportConverter.getReportByInsurance(insurance));
        csvReportService.createReport(reportModels);
    }
}
