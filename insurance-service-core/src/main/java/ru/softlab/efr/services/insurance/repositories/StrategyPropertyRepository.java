package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.insurance.model.db.StrategyProperty;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface StrategyPropertyRepository extends JpaRepository<StrategyProperty, Long> {

    List<StrategyProperty> getByRate(BigDecimal rate);

}
