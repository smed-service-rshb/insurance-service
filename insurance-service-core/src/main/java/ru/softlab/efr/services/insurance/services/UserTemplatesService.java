package ru.softlab.efr.services.insurance.services;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.UserTemplateEntity;
import ru.softlab.efr.services.insurance.repositories.UserTemplatesRepository;

import javax.persistence.EntityNotFoundException;

/**
 * Сервис для работы со справочником пользовательских шаблонов документов
 *
 * @author olshansky
 * @since 10.04.2019
 */
@Service
public class UserTemplatesService extends BaseService<UserTemplateEntity> {

    private TemplateService templateService;

    @Autowired
    public UserTemplatesService(UserTemplatesRepository repository, TemplateService templateService) {
        this.repository = repository;
        this.templateService = templateService;
    }

    public UserTemplateEntity findOne(Long id) {
        return findById(id);
    }

    public Page<UserTemplateEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public UserTemplateEntity saveUserTemplate(UserTemplateEntity backEntity) throws Exception {
        return save(backEntity);
    }

    public byte[] getContentById(Long id) throws Exception {
        UserTemplateEntity backEntity = findById(id);
        if (StringUtils.isBlank(backEntity.getTemplateId())) {
            throw new EntityNotFoundException();
        }
        return IOUtils.toByteArray(templateService.getTemplateContent(backEntity.getTemplateId()).getInputStream());
    }
}
