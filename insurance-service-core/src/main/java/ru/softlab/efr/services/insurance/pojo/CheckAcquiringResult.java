package ru.softlab.efr.services.insurance.pojo;

import ru.softlab.efr.services.insurance.model.db.ClientEntity;

import java.util.Optional;

/**
 * Результат проверки на возможность оформления договора клиентом
 */
public class CheckAcquiringResult {

    /**
     * Признак доступности оформления
     */
    private boolean canAcquire;

    /**
     * Клиент
     */
    private Optional<ClientEntity> clientEntity;

    /**
     * Описание причины не доступности
     */
    private String description;

    public CheckAcquiringResult(boolean canAcquire, ClientEntity clientEntity, String description) {
        this.canAcquire = canAcquire;
        this.clientEntity = Optional.ofNullable(clientEntity);
        this.description = description;
    }

    public CheckAcquiringResult(boolean canAcquire, String description) {
        this.canAcquire = canAcquire;
        this.description = description;
    }

    public CheckAcquiringResult(boolean canAcquire, ClientEntity clientEntity) {
        this.canAcquire = canAcquire;
        this.clientEntity = Optional.ofNullable(clientEntity);
    }

    public boolean isCanAcquire() {
        return canAcquire;
    }

    public Optional<ClientEntity> getClientEntity() {
        return clientEntity;
    }

    public String getDescription() {
        return description;
    }
}
