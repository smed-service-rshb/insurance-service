package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.common.settings.entities.SettingEntity;

import java.util.List;

@Repository
public interface CrmSettingsRepository extends JpaRepository<SettingEntity, Long> {

    @Query("select s from SettingEntity s where s.key like 'CRM_SETTINGS_%'")
    @Override
    List<SettingEntity> findAll();
}
