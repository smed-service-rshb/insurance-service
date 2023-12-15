package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.RequiredField;
import ru.softlab.efr.services.insurance.repositories.RequiredFieldRepository;

import java.util.List;

/**
 * Сервис для работы с обязательными полями.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class RequiredFieldService extends BaseService<RequiredField> {

    @Autowired
    public RequiredFieldService(RequiredFieldRepository repository) {
        this.repository = repository;
    }

    public Page<RequiredField> findAllExceptDeleted(Pageable pageable) {
        return ((RequiredFieldRepository) repository).findAllByDeleted(pageable, false);
    }

    public List<RequiredField> findAllExceptDeleted() {
        return ((RequiredFieldRepository) repository).findAllByDeleted(false);
    }

    public RequiredField findFirstByStrId(String strId) {
        return ((RequiredFieldRepository) repository).findFirstByStrId(strId);
    }
    public List<RequiredField> getChildren(RequiredField requiredField) {
        return ((RequiredFieldRepository) repository).findAllByParentId(requiredField.getId());
    }
}
