package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.OrgUnitClient;
import ru.softlab.efr.services.auth.exceptions.UserOfficeNotFoundException;
import ru.softlab.efr.services.auth.exchange.model.OfficeData;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;

/**
 * Сервис получения данных из справочника орг. структур кэшированием
 */
@Service
@CacheConfig(cacheNames = {"orgunits"})
@PropertySource(value = "classpath:application.properties")
public class OrgUnitCachedClientService {

    private static final Logger LOGGER = Logger.getLogger(OrgUnitCachedClientService.class);

    @Value("${common-dict.interaction.timeout:10}")
    private Long timeout;


    private OrgUnitClient orgUnitClient;

    @Autowired
    public void setOrgUnitClient(OrgUnitClient orgUnitClient) {
        this.orgUnitClient = orgUnitClient;
    }

    @Cacheable
    public OfficeData getById(Long orgUnitId) throws RestClientException {
        ClientRestResult<OfficeData> result = orgUnitClient.getOrgUnit(orgUnitId);
        return result.get(timeout);
    }

    @CacheEvict
    public void evict() {
        LOGGER.info("OrgUnits cache was evicted");
    }
}
