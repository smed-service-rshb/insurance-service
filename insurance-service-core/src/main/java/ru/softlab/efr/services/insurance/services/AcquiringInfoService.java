package ru.softlab.efr.services.insurance.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.AcquiringInfo;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.AcquiringStatus;
import ru.softlab.efr.services.insurance.repositories.AcquiringInfoRepository;

@Service
public class AcquiringInfoService {

    private final AcquiringInfoRepository repository;

    public AcquiringInfoService(AcquiringInfoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AcquiringInfo saveAsNew(AcquiringInfo info){
        info.setStatus(AcquiringStatus.REGISTRATION);
        return repository.save(info);
    }

    @Transactional(readOnly = true)
    public AcquiringInfo findByUuid(String uuid){
        return repository.findByUuid(uuid);
    }

    @Transactional(readOnly = true)
    public AcquiringInfo findByInsurance(Insurance insurance){
        return repository.findTopByInsuranceOrderByCreateDateDesc(insurance);
    }

    @Transactional
    public AcquiringInfo saveAsCreated(String uuid, Insurance insurance){
        AcquiringInfo info = repository.findByUuid(uuid);
        if(info == null || insurance.getId() == null){
            return null;
        }
        info.setInsurance(insurance);
        info.setClientId(insurance.getHolder().getId());
        info.setStatus(AcquiringStatus.CREATED);
        return repository.save(info);
    }

    @Transactional
    public AcquiringInfo saveAsIssued(String uuid){
        AcquiringInfo info = repository.findByUuid(uuid);
        info.setStatus(AcquiringStatus.ISSUED);
        return repository.save(info);
    }

    @Transactional
    public void saveAsError(String uuid, String description){
        AcquiringInfo info = repository.findByUuid(uuid);
        info.setStatus(AcquiringStatus.ERROR);
        info.setDescription(description);
        repository.save(info);
    }

}
