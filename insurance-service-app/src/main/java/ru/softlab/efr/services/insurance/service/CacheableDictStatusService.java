package ru.softlab.efr.services.insurance.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.client.DictStatusClient;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;

/**
 * Сервис получения данных из таблицы загружаемых справочников с кэшированием
 */
@Service
@CacheConfig(cacheNames = {"currentStates"})
@PropertySource(value = "classpath:application.properties")
public class CacheableDictStatusService {

    private static final Logger LOGGER = Logger.getLogger(CacheableDictStatusService.class);

    @Value("${common-dict.interaction.timeout:10}")
    private Long timeout;

    private DictStatusClient dictStatusClient;

    @Autowired
    public void setDictStatusClient(DictStatusClient dictStatusClient) {
        this.dictStatusClient = dictStatusClient;
    }

    @Cacheable
    public String dictDate(Long updateId) throws RestClientException {
        String result = "Нет информации";
        try {
            result = dictStatusClient.getFileDate(updateId, timeout).getFileDate();
            if (result != null) {
                return result;
            }
        } catch (RestClientException | NullPointerException e) {
            LOGGER.error(String.format("Во время получения даты справочника c ID='%s из сервиса common-dict произошла ошибка, причина: %s", updateId, e), e);
        }
        return result;
    }
}