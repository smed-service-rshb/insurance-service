package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import ru.softlab.efr.services.insurance.model.db.ClientResourceEntity;

/**
 * Интерфейс, описывающий CRUD-методы для взаимодествия с таблицей ресурсов клиента
 * @author bazanova
 * @since 30.07.2018
 */
public interface ClientResourceRepository extends JpaRepository<ClientResourceEntity, Long> {

    /**
     * Получить ресурс клиента
     * @param clientId - Id клиента
     * @param resourceKey - Ключ
     * @return Ресурс клиента
     */
    ClientResourceEntity findByClientIdAndResourceKey(String clientId, String resourceKey);

    /**
     * Удаление ресурсов клиента
     * @param clientId - Id клиента
     * @param resourceKey - Ключ
     */
    @Modifying
    void deleteByClientIdAndResourceKey(String clientId, String resourceKey);
}
