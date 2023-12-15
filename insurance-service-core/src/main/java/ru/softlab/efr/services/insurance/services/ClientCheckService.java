package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientCheck;
import ru.softlab.efr.services.insurance.model.db.ClientShortData;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.CheckUnitTypeEnum;
import ru.softlab.efr.services.insurance.repositories.ClientCheckDTO;
import ru.softlab.efr.services.insurance.repositories.ClientCheckRepository;
import ru.softlab.efr.services.insurance.repositories.InsuranceSummary;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с результатами проверки клиента по базам террористов/замороженных/недействительных паспортов/...
 */
@Service
public class ClientCheckService {

    private ClientCheckRepository repository;

    @Autowired
    public void setRepository(ClientCheckRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClientCheck save(ClientCheck clientCheck) {
        clientCheck.setCreationDate(LocalDateTime.now());
        return repository.saveAndFlush(clientCheck);
    }

    @Transactional
    public ClientCheck findById(Long checkId) {
        return repository.findOne(checkId);
    }

    @Transactional(readOnly = true)
    public List<ClientCheck> findAllChecksByClientId(Long clientId) {
        return repository.findAllChecksByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<InsuranceSummary> findAllContracts(Long clientId) {
        return repository.findAllContracts(clientId);
    }

    @Transactional(readOnly = true)
    public List<ClientShortData> findAllSuspiciousClients(CheckUnitTypeEnum type, List<Long> clientIds) {
        if (clientIds == null) {
            return repository.findAllSuspiciousClients(type);
        } else {
            return repository.findAllSuspiciousClients(type, clientIds);
        }
    }

    @Transactional(readOnly = true)
    public List<ClientCheckDTO> findAllTerroristsAndBlocked(CheckUnitTypeEnum type, List<Long> clientIds) {
        if (clientIds == null) {
            return repository.findAllTerroristsAndBlocked(type);
        } else {
            return repository.findAllTerroristsAndBlocked(type, clientIds);
        }
    }

    @Transactional(readOnly = true)
    public Integer countByType(CheckUnitTypeEnum type, List<Long> clientIds) {
        if(clientIds == null){
            return repository.countByType(type);
        } else {
            return repository.countByType(type, clientIds);
        }
    }

    @Transactional(readOnly = true)
    public List<Insurance> findAllContractsWithAllInfo(Long clientId) {
        return repository.findAllContractsWithAllInfo(clientId);
    }
}
