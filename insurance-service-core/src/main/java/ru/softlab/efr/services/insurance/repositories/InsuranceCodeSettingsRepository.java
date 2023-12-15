package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.softlab.efr.common.settings.entities.SettingEntity;

import java.util.List;


public interface InsuranceCodeSettingsRepository extends JpaRepository<SettingEntity, Long> {

    @Query("select s from SettingEntity s where s.key = 'INSURANCE_CODE_SETTINGS_NUMBER'")
    @Override
    List<SettingEntity> findAll();
}
