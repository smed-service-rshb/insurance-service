package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.TopicRequestEntity;

import java.util.List;

@Repository
public interface TopicRequestRepository extends JpaRepository<TopicRequestEntity, Long> {

    List<TopicRequestEntity> getByIsActive(boolean active);

    List<TopicRequestEntity> findAll();

    TopicRequestEntity getById(Long topicId);

    TopicRequestEntity getByIdAndIsActive(Long topicId, boolean active);

    Page<TopicRequestEntity> findAll(Pageable pageable);

}
