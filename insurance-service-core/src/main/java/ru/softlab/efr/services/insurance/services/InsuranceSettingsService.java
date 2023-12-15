package ru.softlab.efr.services.insurance.services;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.common.settings.services.SettingsService;
import ru.softlab.efr.services.insurance.Constants;

import java.util.Objects;

/**
 * Обёртка сервиса для работы с настройками БД
 *
 * @author olshansky
 * @since 29.07.2019
 */
@Primary
@Service
public class InsuranceSettingsService extends SettingsService {

    protected static final Logger LOGGER = Logger.getLogger(InsuranceSettingsService.class);

    public String getReportStoreFolder(String storePath) {
        String result = getValueOrDefault(storePath, Constants.DEFAULT_REPORT_RESULT_STORE_PATH);
        if (result.endsWith("/")) {
            return result;
        }
        return result.concat("/");
    }

    public String getValueOrDefault(String key, String defaultVal) {
        if (!exists(key)) {
            return logWarnAndReturnDefaultValue(key, defaultVal);
        }
        SettingEntity settingEntity = get(key);
        if (Objects.isNull(settingEntity) || StringUtils.isBlank(settingEntity.getValue())) {
            return logWarnAndReturnDefaultValue(key, defaultVal);
        }
        return settingEntity.getValue();
    }

    private String logWarnAndReturnDefaultValue(String key, String defaultVal) {
        LOGGER.warn(String.format("Не удалось прочитать значение настройки %s, будет использовано значение " +
                "по-умолчанию: %s", key, defaultVal));
        return defaultVal;
    }
}
