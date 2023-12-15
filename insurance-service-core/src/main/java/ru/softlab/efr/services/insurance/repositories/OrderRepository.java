package ru.softlab.efr.services.insurance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.insurance.model.db.OrderEntity;

/**
 * Репозиторий для работы с сущностью заказов на оплату договоров страхования.
 *
 * @author olshansky
 * @since 19.02.2019
 */
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findById(Long id);
}