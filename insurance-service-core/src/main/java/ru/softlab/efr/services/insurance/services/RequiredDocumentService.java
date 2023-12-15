package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.RequiredDocument;
import ru.softlab.efr.services.insurance.repositories.RequiredDocumentRepository;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

/**
 * Сервис для работы со справочником обязательных документов.
 *
 * @author Kalantaev
 * @since 14.09.2018
 */
@Service
public class RequiredDocumentService {

    @Resource
    private RequiredDocumentRepository documentRepository;

    /**
     * Найти запись справочника по идентификатору
     *
     * @param id идентификатор обязательного документа
     * @return запись справочника
     */
    @Transactional(readOnly = true)
    public RequiredDocument findById(Long id) {
        return documentRepository.findRequiredDocumentByIdAndDeleted(id, false);
    }
    /**
     * Найти запись справочника по типу
     *
     * @param type наименование обязательного документа
     * @return запись справочника
     */
    @Transactional(readOnly = true)
    public RequiredDocument findByType(String type) {
        return documentRepository.findRequiredDocumentByTypeAndDeleted(type, false);
    }
    /**
     * Получить постранично все неудаленные записи справочника
     * @param pageable параметры постраничного вывода
     * @return страница с записями справочника
     */
    @Transactional(readOnly = true)
    public Page<RequiredDocument> findAll(Pageable pageable) {
        return documentRepository.findAll(pageable, false);
    }

    /**
     * Логическое удаление сущности записи справочника
     *
     * @param id идентификатор сущности
     * @return true запись удалена, false - запись не удалена
     */
    @Transactional
    public boolean logicalDelete(Long id) {
        RequiredDocument document = documentRepository.findOne(id);
        if (document == null || document.getDeleted()) {
            throw new EntityNotFoundException();
        }
        document.setDeleted(true);
        documentRepository.save(document);
        return true;
    }

    /**
     * Создание документа в справочнике
     *
     * @param document сущность документа
     * @return сохраненный документ
     */
    @Transactional
    public RequiredDocument create(RequiredDocument document) {
        return documentRepository.save(document);
    }

    /**
     * Обновление документа в справочнике
     *
     * @param document сущность документа
     * @return обновленный документ
     */
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public RequiredDocument update(RequiredDocument document) {
        RequiredDocument requiredDocument =
                documentRepository.findRequiredDocumentByIdAndDeleted(document.getId(), false);
        if (requiredDocument == null) {
            throw new EntityNotFoundException();
        }
        return documentRepository.save(document);
    }
}
