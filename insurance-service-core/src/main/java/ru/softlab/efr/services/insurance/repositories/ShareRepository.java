package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.ShareEntity;

public interface ShareRepository extends JpaRepository<ShareEntity, Long> {

}
