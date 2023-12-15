package ru.softlab.efr.services.insurance.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.softlab.efr.services.insurance.services.crmexport.CrmExportService;

@Component
public class CrmExportTask {

    private CrmExportService crmExportService;

    @Autowired
    public CrmExportTask(CrmExportService crmExportService) {
        this.crmExportService = crmExportService;
    }

    @Scheduled(cron = "${crm.update.schedule.cron: 0 0 6 * * *}")
    public void execute() {
        crmExportService.exportMadeInsurancesToCrmFolder();
    }

}
