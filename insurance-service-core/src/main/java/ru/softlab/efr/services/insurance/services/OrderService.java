package ru.softlab.efr.services.insurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.OrderEntity;
import ru.softlab.efr.services.insurance.model.rest.OrderData;
import ru.softlab.efr.services.insurance.repositories.OrderRepository;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;


/**
 * Сервис для работы с заказами на оплату договоров страхования.
 *
 * @author olshansky
 * @since 19.02.2019
 */
@Service
public class OrderService {

    final OrderRepository repository;
    private final InsuranceService insuranceService;
    private final ClientService clientService;

    @Autowired
    public OrderService(OrderRepository repository, InsuranceService insuranceService, ClientService clientService) {
        this.repository = repository;
        this.insuranceService = insuranceService;
        this.clientService = clientService;
    }

    @Modifying
    public OrderEntity create(OrderData orderFront) {
        Insurance insurance = null;
        ClientEntity client = null;

        if (orderFront.getContract() != null) {
            insurance = insuranceService.findById(orderFront.getContract());
        }
        if (orderFront.getClient() != null) {
            client = clientService.get(orderFront.getClient());
        }

        OrderEntity order = new OrderEntity(insurance, client, orderFront.getAmount(), orderFront.getCurrency());
        return repository.saveAndFlush(order);
    }

    @Modifying
    public OrderEntity update(Long orderId, OrderEntity orderFront) {
        OrderEntity order = repository.findOne(orderId);
        if (order == null) {
            throw new EntityNotFoundException();
        }
        AppUtils.mapSimilarObjects(orderFront, order);
        order.setModifiedDate(LocalDateTime.now());
        return repository.saveAndFlush(order);
    }

    @Transactional(readOnly = true)
    public OrderEntity getById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<OrderEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
