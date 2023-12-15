package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.AcquiringInfo;
import ru.softlab.efr.services.insurance.model.db.Insurance;

public interface AcquiringInfoRepository extends JpaRepository<AcquiringInfo, String> {

    AcquiringInfo findByUuid(String uuid);

    AcquiringInfo findTopByInsuranceOrderByCreateDateDesc(Insurance insurance);
}
