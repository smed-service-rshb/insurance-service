package ru.softlab.efr.services.insurance.services.crmexport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.insurance.repositories.CrmSettingsRepository;

import java.util.List;

@Service
public class CrmReportSettingsService {

    private final static String CSV_PATH = "CRM_SETTINGS_CSV_PATH";

    private CrmSettingsRepository crmSettingsRepository;

    @Autowired
    public CrmReportSettingsService(CrmSettingsRepository crmSettingsRepository) {
        this.crmSettingsRepository = crmSettingsRepository;
    }

    @Transactional(readOnly = true)
    public String getCsvPath() {
        List<SettingEntity> entityList = crmSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            if (CSV_PATH.equals(settingEntity.getKey())) {
                return settingEntity.getValue();
            }
        }
        return "~/";
    }
}
