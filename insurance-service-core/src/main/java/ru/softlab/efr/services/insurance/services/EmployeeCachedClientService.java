package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.GetEmployeeRs;

/**
 * Сервис получения данных из справочника пользователей с кэшированием
 */
@Service
@CacheConfig(cacheNames = {"employees"})
@PropertySource(value = "classpath:application.properties")
public class EmployeeCachedClientService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeCachedClientService.class);

    @Value("${common-dict.interaction.timeout:10}")
    private Long timeout;

    private EmployeesClient employeesClient;

    @Autowired
    public void setEmployeesClient(EmployeesClient employeesClient) {
        this.employeesClient = employeesClient;
    }

    @Cacheable
    public GetEmployeeRs getById(Long clientId) throws RestClientException {
        return employeesClient.getEmployeeById(clientId, timeout);
    }

    @CacheEvict
    public void evict() {
        LOGGER.info("Employees cache was evicted");
    }
}
