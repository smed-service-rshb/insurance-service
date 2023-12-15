package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientResourceEntity;
import ru.softlab.efr.services.insurance.repositories.ClientResourceRepository;

/**
 * Сервис для работы с ресурсами клиента
 * @author bazanova
 * @since 30.07.2018
 */
@Service
public class ClientResourceService {

    private ClientResourceRepository clientResourceRepository;

    /**
     * Конструктор
     * @param clientResourceRepository - Репозиторий для ресурсов клиента
     */
    @Autowired
    public ClientResourceService(ClientResourceRepository clientResourceRepository) {
        this.clientResourceRepository = clientResourceRepository;
    }

    /**
     * Получить аватар/скан документа/скан согласия
     * @param clientId - Id клиента
     * @param resourceKey - Ключ для поиска скана
     * @return Аватар/скан документа/скан согласия
     */
    @Transactional(readOnly = true)
    public ClientResourceEntity getResource(String clientId, String resourceKey) {
        return clientResourceRepository.findByClientIdAndResourceKey(
                clientId, resourceKey);
    }

    /**
     * Сохранение ресурса клиента
     * @param clientResource - Ресурс
     * @return Сущность ресурса
     */
    @Transactional
    public ClientResourceEntity save(ClientResourceEntity clientResource) {
        return clientResourceRepository.save(clientResource);
    }

    /**
     * Удаление ресурсов клиента
     * @param clientId - Id клиента
     * @param resourceKey - Ключ
     */
    @Transactional
    public void delete(String clientId, String resourceKey) {
        clientResourceRepository.deleteByClientIdAndResourceKey(clientId, resourceKey);
    }

}
