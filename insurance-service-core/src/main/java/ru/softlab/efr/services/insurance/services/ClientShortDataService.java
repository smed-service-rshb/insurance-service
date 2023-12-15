package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.repositories.ClientShortDataRepository;

/**
 * Сервис для работы с сокращёнными данными о клиенте.
 *
 * @author Andrey Grigorov
 */
@Service
public class ClientShortDataService {

    @Autowired
    private ClientShortDataRepository clientShortDataRepository;

    @Transactional(readOnly = true)
    public ClientShortData findById(Long id) {
        return clientShortDataRepository.findOne(id);
    }
}
