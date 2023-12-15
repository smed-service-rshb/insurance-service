package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;
import ru.softlab.efr.services.insurance.repositories.RiskSettingRepository;

import java.util.List;

/**
 * Сервис для работы с обязательными полями.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class RiskSettingService extends BaseService<RiskSetting> {

    @Autowired
    public RiskSettingService(RiskSettingRepository repository) {
        this.repository = repository;
    }

    public Page<RiskSetting> findAllExceptDeleted(Pageable pageable){
        return ((RiskSettingRepository) repository).findAllByDeleted(pageable, false);
    }
    public List<RiskSetting> findAllExceptDeleted(){
        return ((RiskSettingRepository) repository).findAllByDeleted(false);
    }
}
