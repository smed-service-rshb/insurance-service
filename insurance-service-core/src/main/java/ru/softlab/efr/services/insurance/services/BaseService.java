package ru.softlab.efr.services.insurance.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.BaseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.List;


/**
 * Базовый сервис для работы с сущностями системы
 *
 * @author olshansky
 * @since 14.10.2018
 */
abstract class BaseService<EntityClass extends BaseEntity> {

    protected JpaRepository<EntityClass, Long> repository;

    @Transactional(readOnly = true)
    public List<EntityClass> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<EntityClass> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public EntityClass findById(Long id) throws EntityNotFoundException {
        return findEntity(id);
    }

    @Transactional(readOnly = true)
    public EntityClass findByIdExceptDeleted(Long id) throws EntityNotFoundException {
        return findEntityExceptDeleted(id);
    }

    @Transactional
    public EntityClass create(EntityClass entity) {
        return repository.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, isolation = Isolation.READ_COMMITTED)
    public EntityClass save(EntityClass entity) {
        return repository.saveAndFlush(entity);
    }

    @Transactional
    public void create(List<EntityClass> entities) {
        repository.save(entities);
        repository.flush();
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void logicalDelete(Long id) {
        EntityClass existingEntity = findEntity(id);
        existingEntity.setDeleted(true);
        repository.saveAndFlush(existingEntity);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(EntityClass entity) throws EntityNotFoundException {
        repository.delete(findEntity(entity.getId()));
    }

    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void delete(Long id) throws EntityNotFoundException {
        repository.delete(findEntity(id));
    }

    EntityClass findEntity(Long id) throws EntityNotFoundException {
        EntityClass existingEntity = repository.findOne(id);
        if (existingEntity == null) {
            throw new EntityNotFoundException();
        }
        return existingEntity;
    }

    EntityClass findEntityExceptDeleted(Long id) throws EntityNotFoundException {
        EntityClass existingEntity = findEntity(id);
        if (existingEntity.getDeleted() == null || existingEntity.getDeleted()) {
            throw new EntityNotFoundException();
        }
        return existingEntity;
    }
}
