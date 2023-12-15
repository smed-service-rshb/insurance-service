package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.RequiredField;
import ru.softlab.efr.services.insurance.model.db.RiskSetting;
import ru.softlab.efr.services.insurance.model.db.Segment;
import ru.softlab.efr.services.insurance.repositories.RequiredFieldRepository;
import ru.softlab.efr.services.insurance.repositories.RiskSettingRepository;
import ru.softlab.efr.services.insurance.repositories.SegmentRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Сервис для работы с сегментами программ страхования
 *
 * @author olshansky
 * @since 14.10.2018
 */
@Service
public class SegmentService extends BaseService<Segment> {

    @Autowired
    public SegmentService(SegmentRepository repository) {
        this.repository = repository;
    }

    public Page<Segment> findAllExceptDeleted(Pageable pageable){
        return ((SegmentRepository) repository).findAllByDeleted(pageable, false);
    }
    public List<Segment> findAllExceptDeleted(){
        return ((SegmentRepository) repository).findAllByDeleted(false);
    }
}
