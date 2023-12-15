package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.Formula;
import ru.softlab.efr.services.insurance.repositories.FormulaRepository;

import java.util.List;

/**
 * Сервис для работы с формулами расчёта по программам страхования.
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class FormulaService extends BaseService<Formula>  {

    @Autowired
    public FormulaService(FormulaRepository repository) {
        this.repository = repository;
    }

    public Page<Formula> findAllExceptDeleted(Pageable pageable){
        return ((FormulaRepository) repository).findAllByDeleted(pageable, false);
    }

    public List<Formula> findAllExceptDeleted(){
        return ((FormulaRepository) repository).findAllByDeleted(false);
    }
}
