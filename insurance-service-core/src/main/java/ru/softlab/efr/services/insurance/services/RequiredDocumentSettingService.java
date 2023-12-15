package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.ProgramSetting;
import ru.softlab.efr.services.insurance.model.db.RequiredDocumentSetting;
import ru.softlab.efr.services.insurance.repositories.ProgramSettingRepository;
import ru.softlab.efr.services.insurance.repositories.RequiredDocumentSettingRepository;

import java.util.List;


/**
 * Сервис для работы с параметрами обязательных документов.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class RequiredDocumentSettingService extends BaseService<RequiredDocumentSetting> {

    @Autowired
    public RequiredDocumentSettingService(RequiredDocumentSettingRepository repository) {
        this.repository = repository;
    }

    public Page<RequiredDocumentSetting> findAllExceptDeleted(Pageable pageable){
        return ((RequiredDocumentSettingRepository) repository).findAllByDeleted(pageable, false);
    }
    public List<RequiredDocumentSetting> findAllExceptDeleted(){
        return ((RequiredDocumentSettingRepository) repository).findAllByDeleted(false);
    }

}
