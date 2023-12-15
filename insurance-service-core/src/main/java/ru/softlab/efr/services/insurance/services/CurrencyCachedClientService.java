package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.client.CurrenciesClient;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;

import java.util.List;

/**
 * Сервис получения данных из справочника валют с кэшированием
 */
@Service
@CacheConfig(cacheNames = {"currencies"})
@PropertySource(value = "classpath:application.properties")
public class CurrencyCachedClientService {

    private static final Logger LOGGER = Logger.getLogger(CurrencyCachedClientService.class);

    @Value("${common-dict.interaction.timeout:10}")
    private Long timeout;


    private CurrenciesClient currenciesClient;

    @Autowired
    public void setCurrenciesClient(CurrenciesClient currenciesClient) {
        this.currenciesClient = currenciesClient;
    }

    @Cacheable
    public Currency getById(Long currencyId) throws RestClientException {
        ClientRestResult<Currency> result = currenciesClient.getCurrency(currencyId);
        return result.get(timeout);
    }

    @Cacheable
    public List<Currency> getAllCurrency() throws RestClientException {
        RestPageImpl<Currency> currencies = currenciesClient.listCurrencies(new PageRequest(0, 100), timeout);

        return currencies.getContent();
    }

    @CacheEvict
    public void evict() {
        LOGGER.info("Currencies cache was evicted");
    }
}
